/* Licensed under Apache-2.0 */
package io.terrible.app.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.terrible.app.domain.MediaFile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class SearchServiceImpl implements SearchService {

  private final RestHighLevelClient restHighLevelClient;

  @Override
  public Flux<MediaFile> search(final String index, final String query) {

    if (StringUtils.isEmpty(query)) {
      return Flux.empty();
    }

    try {
      final SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
      sourceBuilder.query(QueryBuilders.matchQuery("path", query));
      sourceBuilder.from(0);
      sourceBuilder.size(5000);
      sourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));

      final SearchRequest searchRequest = new SearchRequest();
      searchRequest.indices(index);
      searchRequest.source(sourceBuilder);

      final SearchResponse searchResponse =
          restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);

      final int hitSize = Math.toIntExact(searchResponse.getHits().getTotalHits().value);

      final ArrayDeque<MediaFile> results = new ArrayDeque<>(hitSize);

      searchResponse.getHits().forEach(searchHit -> results.add(convertSourceMap(searchHit)));

      return Flux.fromIterable(results);

    } catch (final IOException e) {
      return Flux.error(e);
    }
  }

  @Override
  public Mono<Void> deleteIndex(final String index) {

    restHighLevelClient
        .indices()
        .deleteAsync(new DeleteIndexRequest(index), RequestOptions.DEFAULT, null);

    return Mono.empty();
  }

  private MediaFile convertSourceMap(final SearchHit searchHit) {

    final ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.findAndRegisterModules();

    final MediaFile mediaFile =
        objectMapper.convertValue(searchHit.getSourceAsMap(), MediaFile.class);
    mediaFile.setId(searchHit.getId());

    return mediaFile;
  }
}
