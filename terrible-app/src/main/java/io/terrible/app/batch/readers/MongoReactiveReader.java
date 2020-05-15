/* Licensed under Apache-2.0 */
package io.terrible.app.batch.readers;

/*
 * Copyright 2012-2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import io.terrible.app.domain.MediaFile;
import io.terrible.app.services.MediaFileService;
import java.util.Iterator;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.data.AbstractPaginatedDataItemReader;
import org.springframework.stereotype.Component;

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
