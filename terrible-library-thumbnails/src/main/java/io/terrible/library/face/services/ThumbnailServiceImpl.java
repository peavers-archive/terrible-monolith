/* Licensed under Apache-2.0 */
package io.terrible.library.face.services;

import static org.apache.commons.math3.util.FastMath.round;

import io.terrible.library.face.utils.CommandUtil;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ThumbnailServiceImpl implements ThumbnailService {

  private final ProcessService processService;

  /**
   * Given a video file, divide its time length into the number of thumbnails to create, using
   * FFMPEG jump to those time stamps to grab the closest frame we find.
   */
  @Override
  public ArrayList<String> createThumbnails(final Path input, final Path output, final int count) {

    double duration = calculateDuration(input);

    if (duration == -1) {
      return new ArrayList<>(0);
    } else {
      duration = duration / 60;
    }

    final File outputDirectory = createOutputDirectory(output);
    final ArrayList<String> thumbnails = new ArrayList<>(count);

    for (int i = 1; i <= count; i++) {
      final Path thumbnailLocation = Path.of(String.format("%s/00%d.jpg", outputDirectory, i));

      final double timestamp = (i - 0.5) * (duration / count) * 60;

      try {
        processService.execute(
            CommandUtil.createThumbnail(
                String.valueOf(round(timestamp)),
                input.toFile().getAbsolutePath(),
                thumbnailLocation.toString()));

        thumbnails.add(thumbnailLocation.toString());
      } catch (final IOException | InterruptedException e) {
        log.error("failed to create thumbnail {} {} {}", input, e.getMessage(), e);
      }
    }

    return thumbnails;
  }

  /**
   * Use FFMPEG to calculate the total duration of the video. This is used to work out where to
   * create the thumbnails.
   */
  private double calculateDuration(final Path input) {

    try {
      final String output =
          processService.execute(CommandUtil.calculateDuration(input.toFile().getAbsolutePath()));

      return StringUtils.isNotBlank(output) ? Double.parseDouble(output) : -1;

    } catch (final IOException | InterruptedException e) {
      log.error("failed to calculate duration {} {} {}", input, e.getMessage(), e);

      return -1;
    }
  }

  /**
   * Attempt to create the output directory. If this fails, we break all rules and hard abort the
   * task as we don't care anymore. Don't really do this in the real world.
   */
  private File createOutputDirectory(final Path output) {

    final File outputFile = output.toFile();

    FileUtils.deleteQuietly(outputFile); // Empty the directory if its there.

    if (!outputFile.mkdirs()) {
      log.error("Failed to create output directory {}", outputFile);
    }

    return outputFile;
  }
}
