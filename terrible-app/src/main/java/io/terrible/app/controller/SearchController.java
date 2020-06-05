/* Licensed under Apache-2.0 */
package io.terrible.app.controller;

import io.terrible.app.domain.MediaFile;
import io.terrible.app.services.SearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/search")
@RequiredArgsConstructor
public class SearchController {

  public static final String INDEX = "media-files";

  private final SearchService searchService;

  @GetMapping
  public Flux<MediaFile> search(@RequestParam final String query) {

    return searchService.search(INDEX, query);
  }

  @DeleteMapping
  public Mono<Void> deleteAll() {

    return searchService.deleteIndex(INDEX);
  }
}
