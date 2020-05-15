/* Licensed under Apache-2.0 */
package io.terrible.app.controller;

import io.terrible.app.domain.History;
import io.terrible.app.domain.MediaFile;
import io.terrible.app.services.HistoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
}
