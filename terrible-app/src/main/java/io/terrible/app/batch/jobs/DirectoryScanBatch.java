/* Licensed under Apache-2.0 */
package io.terrible.app.batch.jobs;

import io.terrible.app.batch.processors.FileProcessor;
import io.terrible.app.batch.writers.MongoReactiveWriter;
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
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.IteratorItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;

@Slf4j
@Configuration
@EnableBatchProcessing
@RequiredArgsConstructor
public class DirectoryScanBatch {

  private final ScanService scanService;

  private final JobBuilderFactory jobBuilderFactory;

  private final StepBuilderFactory stepBuilderFactory;

  private final ReactiveMongoTemplate reactiveMongoTemplate;

  @StepScope
  @Bean(name = "directoryScannerReader")
  public ItemReader<MediaFileDto> reader(
      @Value("#{jobParameters['directory']}") final String directory) {

    try {
      return new IteratorItemReader<>(scanService.scanMedia(directory));
    } catch (IOException e) {
      throw new RuntimeException("Stop everything. Unable to read from directory");
    }
  }

  @Bean(name = "directoryScannerProcessor")
  public FileProcessor processor() {

    return new FileProcessor();
  }

  @Bean(name = "directoryScannerWriter")
  public ItemWriter<MediaFile> writer() {

    return new MongoReactiveWriter<>(reactiveMongoTemplate);
  }

  @Bean(name = "directoryScannerStep")
  public Step step() {

    return stepBuilderFactory
        .get("directoryScannerStep")
        .<MediaFileDto, MediaFile>chunk(1)
        .reader(reader(""))
        .processor(processor())
        .writer(writer())
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
}
