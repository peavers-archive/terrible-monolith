/* Licensed under Apache-2.0 */
package io.terrible.app.services;

import io.terrible.app.domain.Directory;
import io.terrible.app.repository.DirectoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class DirectoryServiceImpl implements DirectoryService {

  private final DirectoryRepository repository;

  @Override
  public Flux<Directory> findAll() {

    return repository.findAll();
  }

  @Override
  public Mono<Directory> save(final Directory directory) {

    return repository.save(directory);
  }

  @Override
  public Mono<Directory> findById(final String directoryId) {

    return repository.findById(directoryId);
  }

  @Override
  public Mono<Void> deleteById(final String directoryId) {

    return repository.deleteById(directoryId);
  }
}
