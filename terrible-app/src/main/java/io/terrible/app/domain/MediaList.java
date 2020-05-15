/* Licensed under Apache-2.0 */
package io.terrible.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.HashSet;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/** @author Chris Turner (chris@forloop.space) */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "media-lists")
@JsonIgnoreProperties(ignoreUnknown = true)
public class MediaList {

  @Id private String id;

  private String name;

  @Builder.Default private HashSet<MediaFile> mediaFiles = new HashSet<>();
}
