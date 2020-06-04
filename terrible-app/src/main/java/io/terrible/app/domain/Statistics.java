package io.terrible.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "history")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Statistics {

  private long totalLibraryCount;

  private long totalLibrarySize;

  private long totalThumbnailCount;

  private long totalThumbnailSize;

  private long averageMediaFileSize;

  private long averageThumbnailSize;
}
