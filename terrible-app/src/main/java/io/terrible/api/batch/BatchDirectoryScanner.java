package io.terrible.api.batch;

import com.google.common.net.MediaType;
import io.terrible.api.domain.MediaFile;
import io.terrible.directory.scanner.converters.MediaFileConverter;
import io.terrible.directory.scanner.domain.MediaFileDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.MongoItemWriter;
import org.springframework.batch.item.support.IteratorItemReader;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.File;
import java.nio.file.Files;

@Slf4j
@Configuration
@EnableBatchProcessing
@RequiredArgsConstructor
public class BatchDirectoryScanner {

    private final JobBuilderFactory jobBuilderFactory;

    private final StepBuilderFactory stepBuilderFactory;

    private final MongoTemplate mongoTemplate;

    @Bean(name = "directoryScannerReader")
    public ItemReader<File> reader() {

        return new IteratorItemReader<>(
                FileUtils.listFiles(new File("/media/beast/_other"), TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE));
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

        writer.setCollection("media-files-v2");

        return writer;
    }

    @Bean(name = "directoryScannerJob")
    public Job job() {

        return jobBuilderFactory.get("directoryScannerJob")
                .incrementer(new RunIdIncrementer())
                .flow(step())
                .end()
                .build();
    }

    @Bean(name = "directoryScannerStep")
    public Step step() {

        return stepBuilderFactory.get("directoryScannerStep").<File, MediaFile>chunk(100).reader(reader())
                .processor(processor())
                .writer(writer())
                .build();
    }

    @RequiredArgsConstructor
    static class Processor implements ItemProcessor<File, MediaFile> {

        @Override
        public MediaFile process(final File file) throws Exception {

            final String mimeType = Files.probeContentType(file.toPath());

            //noinspection UnstableApiUsage
            if (StringUtils.isNoneEmpty(mimeType) && !file.getAbsolutePath().contains("sample") &&
                MediaType.parse(mimeType).is(MediaType.ANY_VIDEO_TYPE)) {

                final MediaFileDto mediaFileDto = MediaFileConverter.convert(file);

                final MediaFile mediaFile = MediaFile.builder().build();

                BeanUtils.copyProperties(mediaFileDto, mediaFile); //Convert DTO to actual.

                return mediaFile;
            }

            return null;
        }

    }

}
