package io.terrible.api.batch;

import io.terrible.api.domain.MediaFile;
import io.terrible.api.utils.FileUtil;
import io.terrible.library.thumbnails.services.ThumbnailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.MongoItemReader;
import org.springframework.batch.item.data.MongoItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Configuration
@EnableScheduling
@EnableBatchProcessing
@RequiredArgsConstructor
public class BatchThumbnailGenerator {

    private final ThumbnailService thumbnailService;

    private final JobBuilderFactory jobBuilderFactory;

    private final StepBuilderFactory stepBuilderFactory;

    private final MongoTemplate mongoTemplate;

    @Bean(name = "thumbnailGeneratorReader")
    public MongoItemReader<MediaFile> reader() {

        final MongoItemReader<MediaFile> reader = new MongoItemReader<>();

        final Map<String, Sort.Direction> map = new HashMap<>();
        map.put("_id", Sort.Direction.DESC);

        reader.setTemplate(mongoTemplate);
        reader.setSort(map);
        reader.setTargetType(MediaFile.class);
        reader.setQuery("{ 'thumbnails.11': { $exists: false} }"); // Find all documents who don't have 12 thumbnails

        return reader;
    }

    @Bean(name = "thumbnailGeneratorProcessor")
    public Processor processor() {

        return new Processor(thumbnailService);
    }

    @Bean(name = "thumbnailGeneratorWriter")
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

    @Bean(name = "thumbnailGeneratorJob")
    public Job job() {

        return jobBuilderFactory.get("thumbnailGeneratorJob")
                .incrementer(new RunIdIncrementer())
                .flow(step())
                .end()
                .build();
    }

    @Bean(name = "thumbnailGeneratorStep")
    public Step step() {

        return stepBuilderFactory.get("thumbnailGeneratorStep").<MediaFile, MediaFile>chunk(10)
                .reader(reader())
                .processor(processor())
                .writer(writer())
                .build();
    }

    @RequiredArgsConstructor
    static class Processor implements ItemProcessor<MediaFile, MediaFile> {

        private final static int NUMBER_OF_THUMBNAILS = 12;

        private final ThumbnailService thumbnailService;

        @Override
        public MediaFile process(final MediaFile mediaFile) {

            mediaFile.setThumbnailPath(FileUtil.getThumbnailDirectory(mediaFile));

            final Path input = Path.of(mediaFile.getPath());
            final Path output = Path.of(mediaFile.getThumbnailPath());

            mediaFile.setThumbnails(thumbnailService.createThumbnails(input, output, NUMBER_OF_THUMBNAILS));

            log.info("Thumbnails ready for media file: {}", mediaFile.getId());

            return mediaFile;
        }
    }
}
