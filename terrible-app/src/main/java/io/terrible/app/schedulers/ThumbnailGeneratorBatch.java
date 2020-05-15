package io.terrible.app.schedulers;

import io.terrible.app.configuration.TerribleConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Slf4j
@Component
@EnableScheduling
@RequiredArgsConstructor
public class ThumbnailGeneratorBatch {

    private final TerribleConfig config;

    private final SimpleJobLauncher simpleJobLauncher;

    @Qualifier("thumbnailGeneratorJob")
    private final Job thumbnailGeneratorJob;

    @Scheduled(fixedDelayString = "${batch.delay}")
    public void schedule()
            throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException,
            JobInstanceAlreadyCompleteException {

        if (config.isThumbnailJob()) {
            log.info("Running job {}", thumbnailGeneratorJob.getName());

            simpleJobLauncher.run(thumbnailGeneratorJob, new JobParametersBuilder().addDate("date", new Date()).toJobParameters());
        }
    }

}
