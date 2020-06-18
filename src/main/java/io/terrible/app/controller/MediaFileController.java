/* Licensed under Apache-2.0 */
package io.terrible.app.controller;

import io.terrible.app.domain.MediaFile;
import io.terrible.app.services.HistoryService;
import io.terrible.app.services.MediaFileService;
import io.terrible.app.services.MediaListService;
import io.terrible.app.services.SearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.File;

@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/media-files")
@RequiredArgsConstructor
public class MediaFileController {

  private final MediaListService mediaListService;

  private final MediaFileService mediaFileService;

  private final HistoryService historyService;

  private final SearchService searchService;

  @GetMapping
  public Flux<MediaFile> findAll(
      @RequestParam(required = false, defaultValue = "createdTime") final String order) {

    return mediaFileService.findAll(order);
  }

  @GetMapping("/{id}")
  public Mono<MediaFile> findById(@PathVariable final String id) {

    return mediaFileService.findById(id);
  }

  @PostMapping
  public Mono<MediaFile> save(@RequestBody final MediaFile mediaFile) {

    return mediaFileService.save(mediaFile);
  }

  @DeleteMapping("/{id}")
  public Mono<Void> deleteById(@PathVariable final String id) {

    return mediaFileService
        .findById(id)
        .doOnSuccess(mediaFile -> FileUtils.deleteQuietly(new File(mediaFile.getPath())))
        .doOnSuccess(mediaFile -> FileUtils.deleteQuietly(new File(mediaFile.getThumbnailPath())))
        .doOnSuccess(mediaFile -> searchService.deleteById("media-files", mediaFile.getId()))
        .then(mediaFileService.deleteById(id));
  }

  @DeleteMapping
  public Mono<Void> deleteAll() {

    return mediaFileService
        .deleteAll()
        .then(mediaListService.deleteAll())
        .then(historyService.deleteAll());
  }
}
