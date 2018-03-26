package com.allardworks.workinator3.consumer;

import com.allardworks.workinator3.core.Assignment;
import com.allardworks.workinator3.core.ServiceStatus;
import com.allardworks.workinator3.core.Status;
import com.allardworks.workinator3.core.WorkerStatus;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.time.Duration;
import java.time.LocalTime;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Runtime context for a worker. This is passed to the worker.
 * The worker uses it to communicate with the workinator.
 */
@RequiredArgsConstructor
public class Context implements WorkerContext {
    @NonNull private final Assignment assignment;
    @NonNull private final WorkerStatus workerStatus;
    @NonNull private final Supplier<ServiceStatus> executorStatus;
    @NonNull private final Function<Context, Boolean> canContinue;

    @NonNull private final LocalTime startDate = LocalTime.now();

    /**
     * The worker reports to the workinator that there is more work to do.
     * This is important for prioritization. It defaults to false.
     * If set to true, then the workinator will give the assigned partition priority
     * over partitions that don't have work.
     * @param hasWork
     */
    public void hasWork(boolean hasWork) {
        workerStatus.setHasWork(hasWork);
    }

    public boolean hasWork() {
        return workerStatus.isHasWork();
    }

    /**
     * Gets the length of time that the context has been alive.
     * @return
     */
    public Duration getElapsed() {
        return Duration.between(startDate, LocalTime.now());
    }

    /**
     * Gets the executors current status.
     * @return
     */
    public Status getExecutorStatus() {
        return executorStatus.get().getStatus();
    }

    /**
     * Returns true if the worker may continue working.
     * Returns false when the worker needs to stop.
     * @return
     */
    @Override
    public boolean canContinue() {
        return canContinue.apply(this);
    }

    /**
     * Returns the worker's assignment.
     * @return
     */
    @Override
    public Assignment getAssignment() {
        return assignment;
    }
}
