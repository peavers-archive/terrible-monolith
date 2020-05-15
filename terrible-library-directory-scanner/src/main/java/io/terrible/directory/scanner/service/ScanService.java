/* Licensed under Apache-2.0 */
package io.terrible.directory.scanner.service;

import io.terrible.directory.scanner.domain.MediaFileDto;
import java.io.IOException;
import java.util.ArrayDeque;

public interface ScanService {

  ArrayDeque<MediaFileDto> scanMedia(String directory) throws IOException;
}
