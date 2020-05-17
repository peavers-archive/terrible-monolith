/* Licensed under Apache-2.0 */
package io.terrible.directory.scanner.service;

import io.terrible.directory.scanner.domain.MediaFileDto;

import java.io.File;
import java.io.IOException;
import java.util.ArrayDeque;

public interface ScanService {

  ArrayDeque<MediaFileDto> scanVideos(String directory) throws IOException;

  ArrayDeque<File> scanPictures(String directory) throws IOException;

}
