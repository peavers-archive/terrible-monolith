/* Licensed under Apache-2.0 */
package io.terrible.api.services;

import io.terrible.api.domain.GroupedMediaFile;
import io.terrible.api.domain.MediaFile;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface MediaFileService {

  Flux<MediaFile> findAll();

  Flux<MediaFile> findAllByOrderBySizeDesc(int limit);

  Flux<MediaFile> findAllByThumbnailsIsNull();

  Flux<GroupedMediaFile> findAllGroupedByDate(String dateField);

  Mono<MediaFile> findById(String id);

  Mono<MediaFile> findByPath(String absolutePath);

  Mono<MediaFile> save(MediaFile mediaFile);

  Flux<MediaFile> saveAll(Flux<MediaFile> mediaFiles);

  Mono<Void> deleteAll();
}
