package com.allardworks.workinator3.consumer;

import com.allardworks.workinator3.core.ConsumerConfiguration;
import com.allardworks.workinator3.core.ConsumerId;
import com.allardworks.workinator3.core.Workinator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Creates an instance of the WorkinatorConsumer.
 * This is a convenience in cases where a single program
 * wants to createPartitions multiple consumers. Usually, that's not how WorkinatorConsumer should
 * be used. But, for tests and demos, you may need multiple.
 * In a usual program, you would just create the beans and inject WorkinatorConsumer. Let spring do the work.
 */
@Component
@RequiredArgsConstructor
public class WorkinatorConsumerFactory {
    private final ConsumerConfiguration consumerConfiguration;
    private final Workinator workinator;
    private final ExecutorFactory executorFactory;
    private final WorkerFactory workerFactory;

    public WorkinatorConsumer create(final ConsumerId id) {
        return new WorkinatorConsumer(consumerConfiguration, workinator, executorFactory, workerFactory, id);
    }
}
