package com.allardworks.workinator3.consumer;

import lombok.Data;
import lombok.NonNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

/**
 * DURATION isn't working with constructors.
 * Binding doesn't work for private setters.
 * So, this class isn't how I would like it.
 * I want to make these properties FINAL.
 * Can't use the builder either.... this sucks.
 */

@Data
@Configuration
@ConfigurationProperties("consumer")
public class ConsumerConfiguration {
    /**
     * The minimum amount of time that a worker will work
     * without interruption, as long as it has work.
     */
    @NonNull
    private Duration minWorkTime = Duration.ofSeconds(30);

    /**
     * If there isn't an assignment for a worker,
     * then delay before checking agian.
     */
    @NonNull
    private Duration delayWhenNoAssignment = Duration.ofSeconds(5);

    /**
     * The maximum number of workers the consumer
     * can process at once.
     */
    private int maxWorkerCount = 1;
}