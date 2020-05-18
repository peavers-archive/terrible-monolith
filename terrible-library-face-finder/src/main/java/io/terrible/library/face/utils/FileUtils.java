package io.terrible.library.face.utils;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;

@Slf4j
@UtilityClass
public class FileUtils {

  public String createOutputDirectory(final String basePath) {

    final File outputDirectory = new File(String.format("%s/.ai", basePath));

    try {
      org.apache.commons.io.FileUtils.deleteQuietly(outputDirectory);

      org.apache.commons.io.FileUtils.forceMkdir(outputDirectory);

      log.info("Output directory {}", outputDirectory);

      return outputDirectory.getAbsolutePath();

    } catch (IOException e) {
      throw new RuntimeException("Abort. Unable to create output directory");
    }
  }

  public String createOutputFile(String directory, String image) {

    final String outputFile =
        String.format("%s/%s-ai.jpg", directory, FilenameUtils.getBaseName(image));

    log.info("Output file {}", outputFile);

    return outputFile;
  }
}
