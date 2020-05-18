/* Licensed under Apache-2.0 */
package io.terrible.library.face.services;

import java.nio.file.Path;
import java.util.ArrayList;

public interface FaceService {

  void detect(Path image, String output);

  void detect(ArrayList<Path> images, String output);
}
