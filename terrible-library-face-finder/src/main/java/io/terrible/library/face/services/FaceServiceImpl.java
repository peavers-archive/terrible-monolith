/* Licensed under Apache-2.0 */
package io.terrible.library.face.services;

import io.terrible.library.face.utils.Classifier;
import io.terrible.library.face.utils.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.springframework.stereotype.Service;

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
    final String outputDirectory = FileUtils.createOutputDirectory(output);

    process(image.toString(), outputDirectory);
  }

  @Override
  public void detect(ArrayList<Path> images, String output) {
    final String outputDirectory = FileUtils.createOutputDirectory(output);

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

    Imgcodecs.imwrite(FileUtils.createOutputFile(output, input), src);
  }

  private void drawBoxes(MatOfRect faceDetections, Mat src) {

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
}
