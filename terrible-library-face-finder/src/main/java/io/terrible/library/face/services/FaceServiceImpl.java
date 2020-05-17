/* Licensed under Apache-2.0 */
package io.terrible.library.face.services;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class FaceServiceImpl implements FaceService {

  private final HashMap<String, String> classifiers = new HashMap<>();

  public FaceServiceImpl() {
    System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

    classifiers.put("haarcascades_smile", "haarcascades/haarcascade_smile.xml");
    classifiers.put("haarcascades_eye", "haarcascades/haarcascade_eye.xml");
    classifiers.put("haarcascades_frontalface_alt", "haarcascades/haarcascade_frontalface_alt.xml");
    classifiers.put("haarcascades_frontalface_alt2", "haarcascades/haarcascade_frontalface_alt2.xml");
    classifiers.put("haarcascades_cuda_frontalface_alt2", "haarcascades_cuda/haarcascade_frontalface_alt2.xml");
  }

  @Override
  public void detect(ArrayList<Path> images) {
    for (Map.Entry<String, String> entry : classifiers.entrySet()) {

      final String outputDirectory = createOutputDirectory(images.get(0), entry);

      images.forEach(
          image -> {
            Mat src = Imgcodecs.imread(image.toString());

            MatOfRect faceDetections = new MatOfRect();

            final CascadeClassifier classifier = loadClassifier(entry.getValue());

            classifier.detectMultiScale(src, faceDetections);

            drawBoxes(faceDetections, src);

            final String outputFile = createOutputFile(outputDirectory, image);

            Imgcodecs.imwrite(outputFile, src);
          });
    }
  }

  private void drawBoxes(MatOfRect faceDetections, Mat src) {
    Arrays.stream(faceDetections.toArray())
        .forEach(
            rect ->
                Imgproc.rectangle(
                    src,
                    new Point(rect.x, rect.y),
                    new Point(rect.x + rect.width, rect.y + rect.height),
                    new Scalar(0, 0, 255),
                    2));
  }

  private String createOutputDirectory(final Path image, final Map.Entry<String, String> entry) {

    final File outputDirectory =
        new File(String.format("%s/.ai/%s", image.getParent(), entry.getKey()));

    FileUtils.deleteQuietly(outputDirectory);

    try {
      FileUtils.forceMkdir(outputDirectory);

      log.info("Output directory {}", outputDirectory);

      return outputDirectory.getAbsolutePath();

    } catch (IOException e) {
      throw new RuntimeException("Abort. Unable to create output directory");
    }
  }

  private String createOutputFile(String directory, Path image) {

    final String outputFile =
        String.format("%s/%s-ai.jpg", directory, FilenameUtils.getBaseName(image.toString()));

    log.info("Output file {}", outputFile);

    return outputFile;
  }

  private CascadeClassifier loadClassifier(String path) {

    try {
      Resource classifierXml = new ClassPathResource(path);

      log.info("Classifier {}", classifierXml.getFilename());

      return new CascadeClassifier(classifierXml.getFile().getAbsolutePath());
    } catch (IOException e) {
      throw new RuntimeException("Abort. Unable to read a classifier");
    }
  }
}
