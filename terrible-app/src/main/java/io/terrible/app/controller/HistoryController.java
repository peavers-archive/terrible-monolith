/* Licensed under Apache-2.0 */
package io.terrible.app.controller;

import io.terrible.app.domain.History;
import io.terrible.app.domain.MediaFile;
import io.terrible.app.services.HistoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/history")
@RequiredArgsConstructor
public class HistoryController {

  private final HistoryService historyService;

  @PostMapping
  public Mono<History> addToHistory(@RequestBody final MediaFile mediaFile) {

    return historyService.addToHistory(mediaFile);
  }

  @GetMapping
  public Mono<History> getHistory() {

    return historyService.getHistory();
  }

  @DeleteMapping
  public Mono<Void> deleteHistory() {

    return historyService.deleteAll();
  }
}
