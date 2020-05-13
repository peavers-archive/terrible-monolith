/* Licensed under Apache-2.0 */
package io.terrible.api.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "history")
@JsonIgnoreProperties(ignoreUnknown = true)
public class History {

  @Id private String id;

  @Builder.Default private ArrayList<MediaFile> results = new ArrayList<>();
}
