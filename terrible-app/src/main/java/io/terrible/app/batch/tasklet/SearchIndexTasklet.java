/* Licensed under Apache-2.0 */
package io.terrible.app.batch.tasklet;

import io.terrible.app.domain.MediaFile;
import io.terrible.app.services.MediaFileService;
import io.terrible.search.domain.MediaFileDto;
import io.terrible.search.services.SearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.BeanUtils;

@Slf4j
@RequiredArgsConstructor
public class SearchIndexTasklet implements Tasklet {

  public static final String INDEX = "media-files";

  private final SearchService searchService;

  private final MediaFileService mediaFileService;

  @Override
  public RepeatStatus execute(
      final StepContribution contribution, final ChunkContext chunkContext) {

    searchService
        .createIndex(INDEX)
        .doOnSuccess(
            (value) ->
                searchService.populate(INDEX, mediaFileService.findAll().map(this::convertActualToDto)).subscribe())
        .subscribe();

    return null;
  }

  private MediaFileDto convertActualToDto(final MediaFile mediaFile) {

    final MediaFileDto mediaFileDto = MediaFileDto.builder().build();

    BeanUtils.copyProperties(mediaFile, mediaFileDto);

    return mediaFileDto;
  }
}
