/* Licensed under Apache-2.0 */
package io.terrible.app.services;

import io.terrible.app.domain.GroupedMediaFile;
import io.terrible.app.domain.MediaFile;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface MediaFileService {

  Flux<MediaFile> findAll();

  Flux<MediaFile> findAllWithoutThumbnails();

  Flux<GroupedMediaFile> findAllGroupedByDate(String dateField);

  Mono<MediaFile> findById(String id);

  Mono<MediaFile> save(MediaFile mediaFile);

  Mono<Void> deleteAll();
}
