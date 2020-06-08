/* Licensed under Apache-2.0 */
package io.terrible.app.repository;

import io.terrible.app.domain.History;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoryRepository extends ReactiveMongoRepository<History, String> {}
