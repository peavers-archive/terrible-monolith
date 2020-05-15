/* Licensed under Apache-2.0 */
package io.terrible.app.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class TaskExecutorConfig {

  @Bean
  public ThreadPoolTaskExecutor taskExecutor() {

    final int availableProcessors = Runtime.getRuntime().availableProcessors();

    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    executor.setCorePoolSize(availableProcessors / 2);
    executor.setMaxPoolSize(availableProcessors - 2);
    executor.setQueueCapacity(Integer.MAX_VALUE);
    executor.setThreadNamePrefix("job-");
    executor.initialize();

    return executor;
  }
}
