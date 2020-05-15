/* Licensed under Apache-2.0 */
package io.terrible.app.batch.schedulers;

import io.terrible.app.configuration.TerribleConfig;
import io.terrible.app.domain.Directory;
import io.terrible.app.services.DirectoryService;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@EnableScheduling
@RequiredArgsConstructor
public class DirectoryScannerScheduler {

  private final TerribleConfig config;

  private final SimpleJobLauncher simpleJobLauncher;

  private final DirectoryService directoryService;

  private final Job directoryScannerJob;

  @Scheduled(fixedDelayString = "${batch.delay}")
  public void schedule() {

    if (config.isDirectoryJob()) {
      directoryService.findAll().doOnNext(this::execute).subscribe();
    }
  }

  private void execute(final Directory directory) {

    try {
      simpleJobLauncher.run(
          directoryScannerJob,
          new JobParametersBuilder()
              .addDate("date", new Date())
              .addString("directory", directory.getPath())
              .toJobParameters());
    } catch (JobExecutionAlreadyRunningException
        | JobRestartException
        | JobInstanceAlreadyCompleteException
        | JobParametersInvalidException e) {
      log.error("Unable to run {} {} {}", directoryScannerJob.getName(), e.getMessage(), e);
    }
  }
}
