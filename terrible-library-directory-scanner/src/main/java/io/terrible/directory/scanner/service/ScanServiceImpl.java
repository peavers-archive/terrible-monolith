/* Licensed under Apache-2.0 */
package io.terrible.directory.scanner.service;

import com.google.common.net.MediaType;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayDeque;
import java.util.Collection;

import io.terrible.directory.scanner.converters.MediaFileConverter;
import io.terrible.directory.scanner.domain.MediaFileDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScanServiceImpl implements ScanService {

  @Override
  public Flux<MediaFileDto> scanMedia(final String input) throws IOException {

    final Collection<File> files =
            FileUtils.listFiles(new File(input), TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE);

    final ArrayDeque<MediaFileDto> results = new ArrayDeque<>(files.size());

    for (final File file : files) {
      final String mimeType = Files.probeContentType(file.toPath());

      //noinspection UnstableApiUsage
      if (StringUtils.isNoneEmpty(mimeType) && !file.getAbsolutePath().contains("sample") &&
          MediaType.parse(mimeType).is(MediaType.ANY_VIDEO_TYPE)) {

        results.add(MediaFileConverter.convert(file));
      }
    }

    return Flux.fromIterable(results);
  }
}
