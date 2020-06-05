/* Licensed under Apache-2.0 */
package io.terrible.app.services;

import static java.time.temporal.TemporalAdjusters.firstDayOfYear;
import static java.time.temporal.TemporalAdjusters.lastDayOfYear;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.group;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.project;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.sort;
import static org.springframework.data.mongodb.core.query.Criteria.where;

import io.terrible.app.domain.AggregateResponse;
import io.terrible.app.domain.GroupedMediaFile;
import io.terrible.app.domain.MediaFile;
import io.terrible.app.repository.MediaFileRepository;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "media-files")
public class MediaFileServiceImpl implements MediaFileService {

  private final MediaFileRepository repository;

  private final ReactiveMongoTemplate mongoTemplate;

  @Override
  @Cacheable()
  public Flux<MediaFile> findAll() {

    log.info("Find all");

    return repository.findAllByOrderByCreatedTimeDesc();
  }

  @Override
  public Mono<MediaFile> findById(final String id) {

    log.info("Find by id {}", id);

    return repository.findById(id);
  }

  @Override
  public Mono<MediaFile> save(final MediaFile mediaFile) {

    log.info("Save {}", mediaFile);

    return repository.save(mediaFile);
  }

  @Override
  public Mono<Void> deleteAll() {

    log.info("Delete all");

    return repository.deleteAll();
  }

  @Override
  public Mono<Long> count() {

    log.info("Media file count");

    return repository.count();
  }

  @Override
  public Mono<Long> totalSize() {

    log.info("Media file total size");

    final Aggregation aggregation = newAggregation(group().sum("size").as("total"));

    return Mono.from(
        mongoTemplate
            .aggregate(aggregation, "media-files", AggregateResponse.class)
            .map(AggregateResponse::getTotal));
  }

  @Override
  public Flux<GroupedMediaFile> findAllGroupedByDate(final String dateField) {

    log.info("Find all grouped by date {}", dateField);

    final LocalDate now = LocalDate.now();
    final LocalDate firstDay = now.with(firstDayOfYear());
    final LocalDate lastDay = now.with(lastDayOfYear());

    final Criteria criteria =
        new Criteria().andOperator(where(dateField).gte(firstDay), where(dateField).lte(lastDay));

    final ProjectionOperation dateProjection =
        project()
            .andInclude("_id", "name", "absolutePath")
            .and(dateField)
            .extractYear()
            .as("year")
            .and(dateField)
            .extractMonth()
            .as("month")
            .and(dateField)
            .extractDayOfMonth()
            .as("day");

    final GroupOperation groupBy =
        group("year", "month", "day")
            .addToSet(
                new Document("id", new Document("$toString", "$_id"))
                    .append("name", "$name")
                    .append("path", "$path"))
            .as("results");

    final Aggregation aggregation =
        newAggregation(
            match(criteria),
            dateProjection,
            groupBy,
            sort(Sort.Direction.DESC, "year", "month", "day"));

    return mongoTemplate.aggregate(aggregation, "media-files", GroupedMediaFile.class);
  }
}
