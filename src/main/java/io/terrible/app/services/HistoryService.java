/* Licensed under Apache-2.0 */
package io.terrible.app.services;

import io.terrible.app.domain.History;
import io.terrible.app.domain.MediaFile;
import reactor.core.publisher.Mono;

public interface HistoryService {

  Mono<History> addToHistory(MediaFile mediaFile);

  Mono<History> getHistory();

  Mono<Void> deleteAll();
}
