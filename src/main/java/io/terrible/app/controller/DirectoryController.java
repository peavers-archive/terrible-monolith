/* Licensed under Apache-2.0 */
package io.terrible.app.controller;

import io.terrible.app.domain.Directory;
import io.terrible.app.services.DirectoryService;
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
import reactor.core.publisher.Mono;

@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/directory")
@RequiredArgsConstructor
public class DirectoryController {

  private final DirectoryService directoryService;

  @GetMapping
  public Mono<Directory> findAll() {

    return directoryService.findAll().next();
  }

  @PostMapping
  public Mono<Directory> save(@RequestBody final Directory directory) {

    return directoryService.save(directory);
  }

  @GetMapping("/{id}")
  public Mono<Directory> findById(@PathVariable final String id) {

    return directoryService.findById(id);
  }

  @DeleteMapping("/{id}")
  public Mono<Void> deleteById(@PathVariable final String id) {

    return directoryService.deleteById(id);
  }
}
