package io.terrible.app.batch.processors;

import io.terrible.app.domain.MediaFile;
import io.terrible.directory.scanner.domain.MediaFileDto;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.BeanUtils;

@RequiredArgsConstructor
public class FileProcessor implements ItemProcessor<MediaFileDto, MediaFile> {

    @Override
    public MediaFile process(final MediaFileDto mediaFileDto) {

        final MediaFile mediaFile = MediaFile.builder().build();

        BeanUtils.copyProperties(mediaFileDto, mediaFile);

        return mediaFile;
    }
}
