/* Licensed under Apache-2.0 */
package io.terrible.app.repository;

import io.terrible.app.domain.MediaFile;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface MediaFileRepository extends ReactiveMongoRepository<MediaFile, String> {

  Flux<MediaFile> findAllByOrderByCreatedTimeDesc();

  @Query("{ 'thumbnails.11': { $exists: false} }")
  Flux<MediaFile> findAllWithoutThumbnails();
}
