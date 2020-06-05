/* Licensed under Apache-2.0 */
package io.terrible.search.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.terrible.search.domain.MediaFileDto;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.search.SearchHit;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JsonUtil {

  private static ObjectMapper objectMapper() {
    final ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.findAndRegisterModules();

    return objectMapper;
  }

  public static String toJson(final MediaFileDto mediaFileDto) {

    try {
      return objectMapper().writeValueAsString(mediaFileDto);
    } catch (final JsonProcessingException e) {
      log.error("Unable to parse to json {}", e.getMessage(), e);

      throw new RuntimeException(e.getMessage());
    }
  }

  public static MediaFileDto convertSourceMap(final SearchHit searchHit) {

    final MediaFileDto mediaFileDto =
        objectMapper().convertValue(searchHit.getSourceAsMap(), MediaFileDto.class);

    mediaFileDto.setId(searchHit.getId());

    return mediaFileDto;
  }
}
