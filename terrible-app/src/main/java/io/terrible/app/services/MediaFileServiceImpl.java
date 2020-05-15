/* Licensed under Apache-2.0 */
package io.terrible.app.services;

import io.terrible.app.domain.GroupedMediaFile;
import io.terrible.app.domain.MediaFile;
import io.terrible.app.repository.MediaFileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

import static java.time.temporal.TemporalAdjusters.firstDayOfYear;
import static java.time.temporal.TemporalAdjusters.lastDayOfYear;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.group;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.project;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.sort;
import static org.springframework.data.mongodb.core.query.Criteria.where;

@Slf4j
@Service
@RequiredArgsConstructor
public class MediaFileServiceImpl implements MediaFileService {

  private final MediaFileRepository repository;

  private final ReactiveMongoTemplate mongoTemplate;

  @Override
  public Flux<MediaFile> findAll() {

    log.info("Find all");

    return repository.findAllByOrderByCreatedTimeDesc();
  }

  @Override
  public Flux<MediaFile> findAllWithoutThumbnails() {

    log.info("Find all without thumbnails");

    return repository.findAllWithoutThumbnails();
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
