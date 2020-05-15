/* Licensed under Apache-2.0 */
package io.terrible.app.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@Data
@ConstructorBinding
@ConfigurationProperties(prefix = "terrible")
public class TerribleConfig {

  private String index;

  private SearchService searchService;

  private boolean thumbnailJob;

  private boolean directoryJob;

  @Data
  public static class SearchService {

    private String scheme;

    private String host;

    private String port;
  }
}
