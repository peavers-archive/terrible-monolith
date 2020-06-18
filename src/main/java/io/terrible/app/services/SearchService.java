/* Licensed under Apache-2.0 */
package io.terrible.app.services;

import io.terrible.app.domain.MediaFile;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface SearchService {

  Flux<MediaFile> search(String index, String query);

  Mono<Void> deleteIndex(String index);

  Mono<Void> deleteById(String index, String id);
}
