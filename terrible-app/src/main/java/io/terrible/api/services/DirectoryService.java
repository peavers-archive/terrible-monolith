/* Licensed under Apache-2.0 */
package io.terrible.api.services;

import io.terrible.api.domain.Directory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface DirectoryService {

  Flux<Directory> findAll();

  Mono<Directory> save(Directory directory);

  Mono<Directory> findById(String directoryId);

  Mono<Void> deleteById(String directoryId);
}
