/* Licensed under Apache-2.0 */
package io.terrible.library.thumbnails.services;

import java.nio.file.Path;
import java.util.ArrayList;

public interface ThumbnailService {

  ArrayList<String> createThumbnails(Path input, Path output, int count);
}
