/* Licensed under Apache-2.0 */
package io.terrible.app.services;

import io.terrible.app.domain.MediaList;
import io.terrible.app.repository.MediaListRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@CacheConfig(cacheNames = "media-files")
@RequiredArgsConstructor
public class MediaListServiceImpl implements MediaListService {

  private final MediaListRepository repository;

  @Override
  @Cacheable()
  public Flux<MediaList> findAll(final String filter) {

    return StringUtils.isNotEmpty(filter)
        ? repository.findAllByNameIsNot(filter)
        : repository.findAll();
  }

  @Override
  public Mono<MediaList> findByNameOrId(final String nameOrId) {

    return repository.findAllByName(nameOrId).switchIfEmpty(repository.findById(nameOrId));
  }

  @Override
  public Mono<MediaList> save(final MediaList mediaList) {

    return repository.save(mediaList);
  }

  @Override
  public Mono<Void> deleteAll() {

    return repository.deleteAll();
  }

  @Override
  public Mono<Void> deleteById(final String id) {

    return repository.deleteById(id);
  }
}
