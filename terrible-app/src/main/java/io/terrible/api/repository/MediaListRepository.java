/* Licensed under Apache-2.0 */
package io.terrible.api.repository;

import io.terrible.api.domain.MediaList;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface MediaListRepository extends ReactiveMongoRepository<MediaList, String> {

  Mono<MediaList> findAllByName(String name);

  Flux<MediaList> findAllByNameIsNot(String filter);
}
