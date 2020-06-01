/* Licensed under Apache-2.0 */
package io.terrible.search.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayDeque;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MediaFileDto {

  private String id;

  private String name;

  private String path;

  private String extension;

  private long size;

  @Builder.Default private ArrayDeque<String> thumbnails = new ArrayDeque<>(12);

  private LocalDateTime createdTime;

  private LocalDateTime lastAccessTime;

  private LocalDateTime lastModifiedTime;
}
