/* Licensed under Apache-2.0 */
package io.terrible.app.services;

import io.terrible.app.domain.MediaList;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/** @author Chris Turner (chris@forloop.space) */
public interface MediaListService {

  Flux<MediaList> findAll(String filter);

  Mono<MediaList> findByNameOrId(String id);

  Mono<MediaList> save(MediaList mediaFile);

  Mono<Void> deleteAll();

  Mono<Void> deleteById(String id);
}
