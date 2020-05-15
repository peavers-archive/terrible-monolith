package io.terrible.app.batch.tasklet;

import io.terrible.app.services.SearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

@Slf4j
@RequiredArgsConstructor
public class SearchIndexTasklet implements Tasklet {

  private final SearchService searchService;

  @Override
  public RepeatStatus execute(final StepContribution contribution, final ChunkContext chunkContext) {
    searchService.createIndex("media").then(searchService.populate("media")).subscribe();

    return null;
  }
}
