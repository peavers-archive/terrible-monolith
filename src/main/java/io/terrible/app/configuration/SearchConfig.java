/* Licensed under Apache-2.0 */
package io.terrible.app.configuration;

import io.terrible.app.properties.SearchProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class SearchConfig {

  @Bean
  public RestHighLevelClient client(final SearchProperties searchProperties) {

    return new RestHighLevelClient(
        RestClient.builder(
            new HttpHost(
                searchProperties.getHost(),
                searchProperties.getPort(),
                searchProperties.getScheme())));
  }
}
