/* Licensed under Apache-2.0 */
package io.terrible.app.batch.processors;

import io.terrible.app.domain.MediaFile;
import io.terrible.app.utils.FileUtil;
import io.terrible.library.face.services.ThumbnailService;
import java.nio.file.Path;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;

@Slf4j
@RequiredArgsConstructor
public class ThumbnailProcessor implements ItemProcessor<MediaFile, MediaFile> {

  private static final int NUMBER_OF_THUMBNAILS = 12;

  private final ThumbnailService thumbnailService;

  @Override
  public MediaFile process(final MediaFile mediaFile) {

    mediaFile.setThumbnailPath(FileUtil.getThumbnailDirectory(mediaFile));

    final Path input = Path.of(mediaFile.getPath());
    final Path output = Path.of(mediaFile.getThumbnailPath());

    mediaFile.setThumbnails(thumbnailService.createThumbnails(input, output, NUMBER_OF_THUMBNAILS));

    log.info("Thumbnails done for: {}", mediaFile.getName());

    return mediaFile;
  }
}
