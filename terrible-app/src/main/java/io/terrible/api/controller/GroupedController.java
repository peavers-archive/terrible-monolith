package io.terrible.api.controller;

import io.terrible.api.domain.GroupedMediaFile;
import io.terrible.api.services.MediaFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import reactor.core.publisher.Flux;

@RequestMapping("group")
@RequiredArgsConstructor
public class GroupedController {

    private final MediaFileService mediaFileService;

    @GetMapping("/media-files")
    public Flux<GroupedMediaFile> group(@RequestParam final String group) {

        return mediaFileService.findAllGroupedByDate(group);
    }

}
