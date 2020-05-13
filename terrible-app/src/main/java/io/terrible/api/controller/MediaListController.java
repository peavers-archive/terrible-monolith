/* Licensed under Apache-2.0 */
package io.terrible.api.controller;

import io.terrible.api.domain.MediaList;
import io.terrible.api.services.MediaListService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/media-lists")
@RequiredArgsConstructor
public class MediaListController {

  private final MediaListService mediaListService;

  @GetMapping("/{id}")
  public Mono<MediaList> findById(@PathVariable final String id) {
    return mediaListService
        .findByNameOrId(id)
        .switchIfEmpty(mediaListService.save(MediaList.builder().name(id).build()));
  }

  @GetMapping
  public Flux<MediaList> findAll(@RequestParam(required = false) final String filter) {
    return mediaListService.findAll(filter);
  }

  @PostMapping
  public Mono<MediaList> save(@RequestBody final MediaList mediaList) {
    return mediaListService.save(mediaList);
  }

  @DeleteMapping("/{id}")
  public Mono<Void> deleteById(@PathVariable final String id) {
    return mediaListService.deleteById(id);
  }

  @DeleteMapping
  public Mono<Void> delete() {
    return mediaListService.deleteAll();
  }
}
