package io.terrible.api.schedulers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
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
public class ThumbnailGeneratorScheduler {

    private final JobLauncher jobLauncher;

    @Qualifier("thumbnailGeneratorJob")
    private final Job thumbnailGeneratorJob;

    @Scheduled(fixedDelayString = "${batch.delay}")
    public void schedule()
            throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException,
            JobInstanceAlreadyCompleteException {

        log.info("Launching new job {}", thumbnailGeneratorJob.getName());

        jobLauncher.run(thumbnailGeneratorJob, new JobParametersBuilder().addDate("date", new Date()).toJobParameters());
    }

}
