/* Licensed under Apache-2.0 */
package io.terrible.api.services;

import io.terrible.api.configuration.TerribleConfig;
import io.terrible.api.domain.MediaFile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class SearchServiceImpl implements SearchService {

  private final TerribleConfig config;

  private final WebClient webClient = WebClient.create();

  @Override
  public Mono<String> createIndex(final String index) {

    final MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
    params.add("name", config.getIndex());

    log.info("Create index {}", params);

    return webClient
        .post()
        .uri(
            uriBuilder ->
                uriBuilder
                    .scheme(config.getSearchService().getScheme())
                    .host(config.getSearchService().getHost())
                    .port(config.getSearchService().getPort())
                    .path("/index")
                    .queryParams(params)
                    .build())
        .retrieve()
        .bodyToMono(String.class);
  }

  @Override
  public Mono<Void> deleteIndex() {

    final MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
    params.add("name", config.getIndex());

    log.info("Delete index {}", params);

    return webClient
        .delete()
        .uri(
            uriBuilder ->
                uriBuilder
                    .scheme(config.getSearchService().getScheme())
                    .host(config.getSearchService().getHost())
                    .port(config.getSearchService().getPort())
                    .pathSegment("index")
                    .queryParams(params)
                    .build())
        .retrieve()
        .bodyToMono(Void.class);
  }

  @Override
  public Mono<String> populate(final String index) {

    final MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
    params.add("name", config.getIndex());

    log.info("Populate {}", params);

    return webClient
        .post()
        .uri(
            uriBuilder ->
                uriBuilder
                    .scheme(config.getSearchService().getScheme())
                    .host(config.getSearchService().getHost())
                    .port(config.getSearchService().getPort())
                    .pathSegment("index")
                    .pathSegment("populate")
                    .queryParams(params)
                    .build())
        .retrieve()
        .bodyToMono(String.class);
  }

  @Override
  public Flux<MediaFile> search(final String query) {

    final MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
    params.add("index", config.getIndex());
    params.add("query", query);

    log.info("Search {}", params);

    return webClient
        .get()
        .uri(
            uriBuilder ->
                uriBuilder
                    .scheme(config.getSearchService().getScheme())
                    .host(config.getSearchService().getHost())
                    .port(config.getSearchService().getPort())
                    .pathSegment("search")
                    .queryParams(params)
                    .build())
        .retrieve()
        .bodyToFlux(MediaFile.class);
  }
}
