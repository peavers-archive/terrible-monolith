/* Licensed under Apache-2.0 */
package io.terrible.search.services;

import io.terrible.search.domain.MediaFileDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface SearchService {

  Mono<Void> createIndex(String index);

  Mono<Void> index(String index, String id, String json);

  Mono<Void> flush();

  Flux<Void> populate(String index, Flux<MediaFileDto> mediaFileDtoFlux);

  Flux<MediaFileDto> search(String index, String query);

  Mono<Void> deleteIndex(String index);
}
