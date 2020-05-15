/* Licensed under Apache-2.0 */
package io.terrible.app.batch.processors;

import io.terrible.app.domain.MediaFile;
import io.terrible.directory.scanner.domain.MediaFileDto;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.BeanUtils;
import org.springframework.lang.NonNull;

@RequiredArgsConstructor
public class FileProcessor implements ItemProcessor<MediaFileDto, MediaFile> {

  @Override
  public MediaFile process(@NonNull final MediaFileDto mediaFileDto) {

    final MediaFile mediaFile = MediaFile.builder().build();

    BeanUtils.copyProperties(mediaFileDto, mediaFile);

    return mediaFile;
  }
}
