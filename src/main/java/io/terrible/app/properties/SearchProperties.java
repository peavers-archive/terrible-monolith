/* Licensed under Apache-2.0 */
package io.terrible.app.properties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ConfigurationProperties("search")
public class SearchProperties {
  private String scheme;

  private String host;

  private int port;
}
