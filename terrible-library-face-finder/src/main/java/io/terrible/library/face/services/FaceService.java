/* Licensed under Apache-2.0 */
package io.terrible.library.face.services;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;

public interface FaceService {

  void detect(ArrayList<Path> images);
}
