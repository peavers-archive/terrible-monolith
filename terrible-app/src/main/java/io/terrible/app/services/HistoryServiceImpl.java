/* Licensed under Apache-2.0 */
package io.terrible.app.services;

import io.terrible.app.domain.History;
import io.terrible.app.domain.MediaFile;
import io.terrible.app.repository.HistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Comparator;

/**
 * History objects contain a list of MediaFile objects. Each time a new object is added to History
 * we look for a History object that fits within the current month. If one is found we append to the
 * list of MediaFiles; if none is found we create a new History object with the year-month ID
 * pairing.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class HistoryServiceImpl implements HistoryService {

  private final HistoryRepository repository;

  @Override
  public Mono<History> addToHistory(final MediaFile mediaFile) {
    final LocalDateTime localDateTime =
        LocalDateTime.ofInstant(Instant.now(), ZoneId.systemDefault());

    final String historyId = getHistoryId();

    mediaFile.setLastWatched(localDateTime);

    return repository
        .findById(historyId)
        .defaultIfEmpty(History.builder().id(historyId).build())
        .doOnNext(history -> history.getResults().add(mediaFile))
        .flatMap(repository::save);
  }

  @Override
  public Mono<History> getHistory() {

    return repository
        .findById(getHistoryId())
        .map(
            history -> {
              history.getResults().sort(Comparator.comparing(MediaFile::getLastWatched).reversed());
              return history;
            });
  }

  @Override
  public Mono<Void> deleteAll() {

    return repository.deleteAll();
  }

  /**
   * Create an ID with a year-month pattern. This is not expected to be unique for each History
   * object as we either want to create a new object each month, or update the current object.
   */
  private String getHistoryId() {
    final LocalDateTime localDateTime =
        LocalDateTime.ofInstant(Instant.now(), ZoneId.systemDefault());

    return String.format("%d-%d", localDateTime.getYear(), localDateTime.getMonthValue());
  }
}
