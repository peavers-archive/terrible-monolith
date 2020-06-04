/* Licensed under Apache-2.0 */
package io.terrible.app.controller;

import io.terrible.app.domain.MediaFile;
import io.terrible.app.services.HistoryService;
import io.terrible.app.services.MediaFileService;
import io.terrible.app.services.MediaListService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;

@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/media-files")
@RequiredArgsConstructor
public class MediaFileController {

  private final MediaListService mediaListService;

  private final MediaFileService mediaFileService;

  private final HistoryService historyService;

  @GetMapping
  public Flux<MediaFile> findAll() {

    return mediaFileService.findAll();
  }

  @GetMapping("/{id}")
  public Mono<MediaFile> findById(@PathVariable final String id) {

    return mediaFileService.findById(id);
  }

  @PostMapping
  public Mono<MediaFile> save(@RequestBody final MediaFile mediaFile) {

    return mediaFileService.save(mediaFile);
  }

  @DeleteMapping
  public Mono<Void> deleteAll() {

    return mediaFileService
        .deleteAll()
        .then(mediaListService.deleteAll())
        .then(historyService.deleteAll());
  }
}
