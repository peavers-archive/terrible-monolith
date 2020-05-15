package io.terrible.app.batch.writers;

import io.terrible.app.domain.MediaFile;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

/** If you've ever seen a hammer, this is the biggest one you could possibly imagine. */
@Component
@RequiredArgsConstructor
public class MongoReactiveWriter<T> implements ItemWriter<MediaFile> {

  private final ReactiveMongoTemplate reactiveMongoTemplate;

  @Override
  public void write(List<? extends MediaFile> items) {
    items.forEach(mediaFile -> reactiveMongoTemplate.save(mediaFile).subscribe());
  }
}
