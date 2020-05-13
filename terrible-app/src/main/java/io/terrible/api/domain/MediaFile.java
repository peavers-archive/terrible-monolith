/* Licensed under Apache-2.0 */
package io.terrible.api.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "media-files")
@JsonIgnoreProperties(ignoreUnknown = true)
public class MediaFile {

  @Id private String id;

  private String name;

  private String path;

  private String thumbnailPath;

  private String extension;

  private long size;

  @Builder.Default private ArrayList<String> thumbnails = new ArrayList<>();

  private LocalDateTime createdTime;

  private LocalDateTime lastAccessTime;

  private LocalDateTime lastModifiedTime;

  private LocalDateTime lastWatched;

  public MediaFile setId(final String id) {

    this.id = id;

    return this;
  }
}
