/* Licensed under Apache-2.0 */
package io.terrible.app.controller;

import io.terrible.app.services.MediaFileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/statistics")
@RequiredArgsConstructor
public class StatisticsController {

  private final MediaFileService mediaFileService;

  @GetMapping("/total-media-file-count")
  public Mono<Long> getTotalMediaFileCount() {

    return mediaFileService.count();
  }

  @GetMapping("/total-directory-size")
  public Mono<Long> getTotalDirectorySize() {

    return mediaFileService.totalSize();
  }
}
