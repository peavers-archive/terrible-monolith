/* Licensed under Apache-2.0 */
package io.terrible.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

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

  private String virtualPath;

  private String thumbnailPath;

  private String extension;

  private long size;

  @Builder.Default private ArrayList<String> thumbnails = new ArrayList<>();

  private LocalDateTime createdTime;

  private LocalDateTime lastAccessTime;

  private LocalDateTime lastModifiedTime;

  private LocalDateTime lastWatched;

  private boolean isIndexed;

  private boolean isIgnored;

  private boolean isDelete;

  public MediaFile setId(final String id) {

    this.id = id;

    return this;
  }

  public String getParent() {
    return new File(this.path).getParent();
  }
}
