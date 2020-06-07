/* Licensed under Apache-2.0 */
package io.terrible.app.repository;

import io.terrible.app.domain.MediaFile;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface MediaFileRepository extends ReactiveMongoRepository<MediaFile, String> {

  @Query("{ 'isIgnored': false }")
  Flux<MediaFile> findAllNotIgnored(Sort sort);

  @Query("{ 'isIgnored': true }")
  Flux<MediaFile> findAllIgnored();
}
