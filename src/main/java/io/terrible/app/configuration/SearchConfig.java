/* Licensed under Apache-2.0 */
package io.terrible.app.configuration;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class SearchConfig {

  @Value("${search.host}")
  public String hostname;

  @Value("${search.port}")
  public int port;

  @Value("${search.scheme}")
  public String scheme;

  @Bean
  public RestHighLevelClient client() {

    return new RestHighLevelClient(RestClient.builder(new HttpHost(hostname, port, scheme)));
  }
}
