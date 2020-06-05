package io.terrible.app.controller;

import io.terrible.app.domain.MediaFile;
import io.terrible.app.services.MediaFileService;
import io.terrible.search.domain.MediaFileDto;
import io.terrible.search.services.SearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.ArrayList;

@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/task")
@RequiredArgsConstructor
public class TaskController {

  private final MediaFileService mediaFileService;

  private final SearchService searchService;

  @GetMapping("/recreate-thumbnails/{id}")
  public Mono<MediaFile> invalidateThumbnails(@PathVariable final String id) {

    return mediaFileService
        .findById(id)
        .doOnNext(mediaFile -> mediaFile.setThumbnails(new ArrayList<>(12)))
        .flatMap(mediaFileService::save);
  }

  @GetMapping("/search/reindex")
  public Mono<Void> searchReindex() {
    return searchService.populate(
        "media-files", mediaFileService.findAll().map(this::convertToDto));
  }

  private MediaFileDto convertToDto(final MediaFile mediaFile) {

    final MediaFileDto mediaFileDto = MediaFileDto.builder().build();
    BeanUtils.copyProperties(mediaFile, mediaFileDto);

    return mediaFileDto;
  }
}
