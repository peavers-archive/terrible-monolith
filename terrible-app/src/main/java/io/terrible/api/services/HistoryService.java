/* Licensed under Apache-2.0 */
package io.terrible.api.services;

import io.terrible.api.domain.History;
import io.terrible.api.domain.MediaFile;
import reactor.core.publisher.Mono;

public interface HistoryService {

  Mono<History> addToHistory(MediaFile mediaFile);

  Mono<History> getHistory();

  Mono<Void> deleteAll();
}
