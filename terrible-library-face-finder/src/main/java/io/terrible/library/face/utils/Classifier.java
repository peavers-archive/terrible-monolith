/* Licensed under Apache-2.0 */
package io.terrible.library.face.utils;

import java.io.IOException;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.opencv.objdetect.CascadeClassifier;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

@Slf4j
@UtilityClass
public class Classifier {

  public final String FRONTAL_FACE_ALT = "haarcascades/haarcascade_frontalface_alt.xml";

  public CascadeClassifier load(String path) {

    try {
      Resource classifierXml = new ClassPathResource(path);

      log.info("Classifier {}", classifierXml.getFilename());

      return new CascadeClassifier(classifierXml.getFile().getAbsolutePath());

    } catch (IOException e) {
      throw new RuntimeException("Abort. Unable to read a classifier");
    }
  }
}
