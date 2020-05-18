/* Licensed under Apache-2.0 */
package io.terrible.library.face.services;

import io.terrible.library.face.utils.Classifier;
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
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;

@Slf4j
@Service
public class FaceServiceImpl implements FaceService {

  private final CascadeClassifier classifier;

  public FaceServiceImpl() {
    System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

    this.classifier = Classifier.load(Classifier.FRONTAL_FACE_ALT);
  }

  @Override
  public void detect(final Path image, final String output) {
    final String outputDirectory = createOutputDirectory(output);

    process(image.toString(), outputDirectory);
  }

  @Override
  public void detect(ArrayList<Path> images, String output) {
    final String outputDirectory = createOutputDirectory(output);

    images.forEach(image -> process(image.toString(), outputDirectory));
  }

  private void process(final String input, final String output) {

    Mat grayFrame = new Mat();
    MatOfRect faces = new MatOfRect();
    Mat src = Imgcodecs.imread(input);

    Imgproc.cvtColor(src, grayFrame, Imgproc.COLOR_BGR2GRAY);
    Imgproc.equalizeHist(grayFrame, grayFrame);

    classifier.detectMultiScale(grayFrame, faces, 1.1, 2);
    drawBoxes(faces, src);

    Imgcodecs.imwrite(createOutputFile(output, input), src);
  }

  private void drawBoxes(MatOfRect faceDetections, Mat src) {

    log.info("Drawing #{} boxes", faceDetections.total());

    Arrays.stream(faceDetections.toArray())
        .forEach(
            rect ->
                Imgproc.rectangle(
                    src,
                    new Point(rect.x, rect.y),
                    new Point(rect.x + rect.width, rect.y + rect.height),
                    new Scalar(0, 255, 0),
                    2));
  }

  private String createOutputDirectory(final String basePath) {

    final File outputDirectory = new File(String.format("%s/.ai", basePath));

    try {
      FileUtils.deleteQuietly(outputDirectory);

      FileUtils.forceMkdir(outputDirectory);

      log.info("Output directory {}", outputDirectory);

      return outputDirectory.getAbsolutePath();

    } catch (IOException e) {
      throw new RuntimeException("Abort. Unable to create output directory");
    }
  }

  private String createOutputFile(String directory, String image) {

    final String outputFile =
        String.format("%s/%s-ai.jpg", directory, FilenameUtils.getBaseName(image));

    log.info("Output file {}", outputFile);

    return outputFile;
  }
}
