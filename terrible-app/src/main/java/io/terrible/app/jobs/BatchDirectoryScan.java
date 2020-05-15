/* Licensed under Apache-2.0 */
package io.terrible.app.jobs;

import io.terrible.app.domain.MediaFile;
import io.terrible.directory.scanner.domain.MediaFileDto;
import io.terrible.directory.scanner.service.ScanService;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.MongoItemWriter;
import org.springframework.batch.item.support.IteratorItemReader;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.mongodb.core.MongoTemplate;

@Slf4j
@Configuration
@EnableBatchProcessing
@RequiredArgsConstructor
public class BatchDirectoryScan {

  private final TaskExecutor taskExecutor;

  private final ScanService scanService;

  private final JobBuilderFactory jobBuilderFactory;

  private final StepBuilderFactory stepBuilderFactory;

  private final MongoTemplate mongoTemplate;

  @StepScope
  @Bean(name = "directoryScannerReader")
  public ItemReader<MediaFileDto> reader(
      @Value("#{jobParameters['directory']}") final String directory) {

    log.info("Directory {}", directory);

    try {
      return new IteratorItemReader<>(scanService.scanMedia(directory));
    } catch (IOException e) {
      throw new RuntimeException("Stop everything. Unable to read from directory");
    }
  }

  @Bean(name = "directoryScannerProcessor")
  public Processor processor() {

    return new Processor();
  }

  @Bean(name = "directoryScannerWriter")
  public ItemWriter<MediaFile> writer() {

    final MongoItemWriter<MediaFile> writer = new MongoItemWriter<>();

    try {
      writer.setTemplate(mongoTemplate);
    } catch (final Exception e) {
      log.error(e.toString());
    }

    writer.setCollection("media-files");

    return writer;
  }

  @Bean(name = "directoryScannerStep")
  public Step step() {

    return stepBuilderFactory
        .get("directoryScannerStep")
        .<MediaFileDto, MediaFile>chunk(1)
        .reader(reader(""))
        .processor(processor())
        .writer(writer())
        .taskExecutor(taskExecutor)
        .build();
  }

  @Bean(name = "directoryScannerJob")
  public Job directoryScannerJob() {

    return jobBuilderFactory
        .get("directoryScannerJob")
        .incrementer(new RunIdIncrementer())
        .flow(step())
        .end()
        .build();
  }

  @RequiredArgsConstructor
  static class Processor implements ItemProcessor<MediaFileDto, MediaFile> {

    @Override
    public MediaFile process(final MediaFileDto mediaFileDto) {

      final MediaFile mediaFile = MediaFile.builder().build();

      BeanUtils.copyProperties(mediaFileDto, mediaFile);

      return mediaFile;
    }
  }
}
