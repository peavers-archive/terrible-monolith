/* Licensed under Apache-2.0 */
package io.terrible.api.services;

import io.terrible.api.domain.MediaFile;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface SearchService {

  Mono<String> createIndex(String index);

  Mono<String> populate(String index);

  Flux<MediaFile> search(String query);

  Mono<Void> deleteIndex();
}
