/* Licensed under Apache-2.0 */
package io.terrible.api.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupedMediaFile {

  private int year;

  private int month;

  private int day;

  private ArrayList<?> results = new ArrayList<>();
}
