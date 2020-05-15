/* Licensed under Apache-2.0 */
package io.terrible.app.batch.jobs;

import io.terrible.app.batch.processors.ThumbnailProcessor;
import io.terrible.app.batch.readers.MongoReactiveReader;
import io.terrible.app.batch.writers.MongoReactiveWriter;
import io.terrible.app.domain.MediaFile;
import io.terrible.app.services.MediaFileService;
import io.terrible.library.thumbnails.services.ThumbnailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.partition.support.SimplePartitioner;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;

@Slf4j
@Configuration
@EnableScheduling
@EnableBatchProcessing
@RequiredArgsConstructor
public class ThumbnailGeneratorBatch {

  private final ThumbnailService thumbnailService;

  private final MediaFileService mediaFileService;

  private final JobBuilderFactory jobBuilderFactory;

  private final StepBuilderFactory stepBuilderFactory;

  private final ReactiveMongoTemplate reactiveMongoTemplate;

  @StepScope
  @Bean(name = "thumbnailGeneratorReader")
  public ItemReader<MediaFile> reader() {

    return new MongoReactiveReader<MediaFile>(mediaFileService);
  }

  @Bean(name = "thumbnailGeneratorProcessor")
  public ThumbnailProcessor processor() {

    return new ThumbnailProcessor(thumbnailService);
  }

  @Bean(name = "thumbnailGeneratorWriter")
  public ItemWriter<MediaFile> writer() {

    return new MongoReactiveWriter<>(reactiveMongoTemplate);
  }

  @Bean(name = "thumbnailGeneratorJob")
  public Job thumbnailGeneratorJob() {

    return jobBuilderFactory
        .get("thumbnailGeneratorJob")
        .incrementer(new RunIdIncrementer())
        .flow(partitionedStep(step()))
        .end()
        .build();
  }

  @Bean(name = "thumbnailGeneratorStep")
  public Step step() {

    return stepBuilderFactory
        .get("thumbnailGeneratorStep")
        .<MediaFile, MediaFile>chunk(3)
        .reader(reader())
        .processor(processor())
        .writer(writer())
        .build();
  }

  @Bean
  public Step partitionedStep(Step thumbnailGeneratorStep) {

    return stepBuilderFactory
        .get("partitionedStep")
        .partitioner(thumbnailGeneratorStep)
        .partitioner("thumbnailGeneratorStep", new SimplePartitioner())
        .gridSize(3)
        .build();
  }
}
