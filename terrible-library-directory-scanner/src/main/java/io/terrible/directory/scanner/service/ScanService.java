/* Licensed under Apache-2.0 */
package io.terrible.directory.scanner.service;

import io.terrible.directory.scanner.domain.MediaFileDto;
import reactor.core.publisher.Flux;

import java.io.IOException;

public interface ScanService {

  Flux<MediaFileDto> scanMedia(String directory) throws IOException;
}
