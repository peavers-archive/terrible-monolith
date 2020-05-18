/* Licensed under Apache-2.0 */
package io.terrible.app.controller;

import io.terrible.directory.scanner.service.ScanService;
import io.terrible.library.face.services.FaceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * Test controller for development.
 *
 * <p>TODO Delete this.
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class FaceController {

  private final FaceService faceService;

  private final ScanService scanService;

  private final String TEST_DIR = "/home/chris/Downloads/test-sample";

  @GetMapping("/face")
  public Mono<Void> detect() throws IOException {

    final ArrayDeque<File> files = scanService.scanPictures(TEST_DIR);

    ArrayList<Path> paths =
        files.stream().map(File::toPath).collect(Collectors.toCollection(ArrayList::new));

    faceService.detect(paths, TEST_DIR);

    return Mono.empty();
  }
}
