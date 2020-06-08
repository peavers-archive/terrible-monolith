/* Licensed under Apache-2.0 */
package io.terrible.app.controller;

import io.terrible.app.domain.MediaFile;
import io.terrible.app.services.MediaFileService;
import java.util.ArrayList;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/task")
@RequiredArgsConstructor
public class TaskController {

  private final MediaFileService mediaFileService;

  @GetMapping("/recreate-thumbnails/{id}")
  public Mono<MediaFile> invalidateThumbnails(@PathVariable final String id) {

    return mediaFileService
        .findById(id)
        .doOnNext(mediaFile -> mediaFile.setThumbnails(new ArrayList<>(12)))
        .flatMap(mediaFileService::save);
  }
}
