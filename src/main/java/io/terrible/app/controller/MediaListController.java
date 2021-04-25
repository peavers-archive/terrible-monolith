/* Licensed under Apache-2.0 */
package io.terrible.app.controller;

import com.google.common.hash.Hashing;
import io.terrible.app.domain.MediaFile;
import io.terrible.app.domain.MediaList;
import io.terrible.app.services.MediaFileService;
import io.terrible.app.services.MediaListService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;

@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/media-lists")
@RequiredArgsConstructor
public class MediaListController {

    private final MediaListService mediaListService;

    private final MediaFileService mediaFileService;

    @GetMapping("/{id}")
    public Mono<MediaList> findById(@PathVariable final String id) {
        return mediaListService
                .findByNameOrId(id)
                .switchIfEmpty(mediaListService.save(MediaList.builder().name(id).build()));
    }

    @GetMapping
    public Flux<MediaList> findAll(@RequestParam(required = false) final String filter) {

        return mediaFileService
                .findAll()
                .groupBy(MediaFile::getParent)
                .flatMap(idFlux -> idFlux.collectList()
                        .map(listOfMediaFiles -> MediaList.builder().id(getId(new File(idFlux.key()))).name(new File(idFlux.key()).getName())
                                .mediaFiles(new HashSet<>(listOfMediaFiles))
                                .build()))
                .flatMap(mediaListService::save);
    }

    @PostMapping
    public Mono<MediaList> save(@RequestBody final MediaList mediaList) {
        return mediaListService.save(mediaList);
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deleteById(@PathVariable final String id) {
        return mediaListService.deleteById(id);
    }

    @DeleteMapping
    public Mono<Void> delete() {
        return mediaListService.deleteAll();
    }

    private String getId(final File file) {
        try {
            //noinspection UnstableApiUsage
            return Hashing.murmur3_128()
                    .hashString(file.getCanonicalPath(), StandardCharsets.UTF_8)
                    .toString();
        } catch (final IOException e) {
            return null;
        }
    }
}
