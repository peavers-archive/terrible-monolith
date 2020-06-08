/* Licensed under Apache-2.0 */
package io.terrible.app.domain;

import java.util.ArrayList;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupedMediaFile {

  private int year;

  private int month;

  private int day;

  private ArrayList<?> results = new ArrayList<>();
}
