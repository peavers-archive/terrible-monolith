/* Licensed under Apache-2.0 */
package io.terrible.api.controller;

import io.terrible.api.domain.MediaFile;
import io.terrible.api.services.MediaFileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.io.File;

@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/static-resource")
@RequiredArgsConstructor
public class StaticResourceController {

  private final MediaFileService mediaFileService;

  @GetMapping(value = "/image", produces = MediaType.IMAGE_JPEG_VALUE)
  public Mono<Resource> image(@RequestParam final String path) {

    return Mono.just(new FileSystemResource(new File(path)));
  }

  @GetMapping(value = "/video", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
  public Mono<Resource> video(@RequestParam final String id) {

    return mediaFileService.findById(id).map(MediaFile::getPath).map(FileSystemResource::new);
  }
}
