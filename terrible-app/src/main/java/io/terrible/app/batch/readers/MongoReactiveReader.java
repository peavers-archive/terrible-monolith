/* Licensed under Apache-2.0 */
package io.terrible.app.batch.readers;

import io.terrible.app.domain.MediaFile;
import io.terrible.app.services.MediaFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.data.AbstractPaginatedDataItemReader;
import org.springframework.stereotype.Component;

import java.util.Iterator;

/** If you've ever seen a hammer, this is the biggest one you could possibly imagine. */
@Component
@RequiredArgsConstructor
public class MongoReactiveReader<T> extends AbstractPaginatedDataItemReader<MediaFile> {

  private final MediaFileService mediaFileService;

  @Override
  protected Iterator<MediaFile> doPageRead() {
    return mediaFileService.findAllWithoutThumbnails().toIterable().iterator();
  }
}
