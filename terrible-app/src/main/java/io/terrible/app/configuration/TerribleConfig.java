/* Licensed under Apache-2.0 */
package io.terrible.app.configuration;

import io.terrible.search.services.SearchService;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@Data
@ConstructorBinding
@ConfigurationProperties(prefix = "terrible")
public class TerribleConfig {

  private String index;

  private boolean thumbnailJob;

  private boolean directoryJob;

}
