package com.allardworks.workinator3.consumer;

import com.allardworks.workinator3.core.ConsumerConfiguration;
import com.allardworks.workinator3.core.WorkerId;
import com.allardworks.workinator3.core.Workinator;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ExecutorFactory {
    @NonNull
    private final ConsumerConfiguration configuration;

    @NonNull
    private final Workinator workinator;

    /**
     * Creates an executor for the type of worker returned by the worker factory.
     * @param workerId
     * @param workerFactory used to determine what type of executor to create.
     * @return
     */
    public ExecutorAsync createExecutor(@NonNull WorkerId workerId, @NonNull final WorkerFactory workerFactory) {
        if (workerFactory instanceof AsyncWorkerFactory) {
            return new ExecutorAsync(workerId, configuration, (AsyncWorkerFactory) workerFactory, workinator);
        }

        throw new RuntimeException("Unknown type of WorkerFactory. The factory must implement AsyncWorkerFactory.");
    }
}
