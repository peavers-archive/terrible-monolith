/* Licensed under Apache-2.0 */

package io.terrible.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

import javax.annotation.PostConstruct;

@Slf4j
@ConfigurationPropertiesScan
@SpringBootApplication(scanBasePackages = "io.terrible.*")
public class Application {

    public static void main(final String[] args) {

        SpringApplication.run(Application.class, args);
    }

    @PostConstruct
    public void init() {

        log.info("Running across {} threads", Runtime.getRuntime().availableProcessors());
    }

//    @Bean
//    @Qualifier("job-pool")
//    public ThreadPoolTaskExecutor taskExecutor() {
//
//        final int availableProcessors = Runtime.getRuntime().availableProcessors();
//
//        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
//        executor.setCorePoolSize(availableProcessors / 2);
//        executor.setMaxPoolSize(availableProcessors - 2);
//        executor.setQueueCapacity(Integer.MAX_VALUE);
//        executor.setThreadNamePrefix("job-");
//        executor.initialize();
//
//        return executor;
//    }

}

