package com.allardworks.workinator3.consumer;

import com.allardworks.workinator3.core.ServiceBase;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

import java.util.HashMap;
import java.util.Map;

/**
 * Creates one thread per worker.
 */
@Slf4j
public class ExecutorAsync extends ServiceBase {
    private final WorkerRunnerProvider runnerProvider;
    private final WorkerId id;
    private final ConsumerConfiguration consumerConfiguration;
    private final WorkerStatus workerStatus;
    private Thread thread;

    public ExecutorAsync(
            final WorkerId workerId,
            final ConsumerConfiguration configuration,
            final AsyncWorkerFactory workerFactory,
            final Workinator workinatorRepository) {

        workerStatus = new WorkerStatus(workerId);
        consumerConfiguration = configuration;
        runnerProvider = new WorkerRunnerProvider(this::canContinue, workerFactory, workinatorRepository, workerStatus, getServiceStatus());
        id = workerId;
    }

    public WorkerStatus getWorkerStatus() {
        return workerStatus.clone();
    }

    private boolean canContinue(final Context context) {
        return getStatus().isStarted() && context.getElapsed().compareTo(consumerConfiguration.getMinWorkTime()) < 0;
    }

    public Map<String, Object> getInfo() {
        val info = new HashMap<String, Object>();
        info.put("serviceStatus", getStatus().toString());
        info.put("executorId", id);
        info.put("currentAssignment", runnerProvider.getCurrentAssignment());
        info.put("currentContext", runnerProvider.getCurrentContext());
        return info;
    }

    private void run() {
        // The executor runs as long as the service is started.
        // the assignment it executes may change many times, but the thread nor executor stops.
        try {
            getServiceStatus().started();
            while (getServiceStatus().getStatus().isStarted()) {
                val runner = runnerProvider.lookupRunner();
                if (runner == null) {
                    // TODO: need a ManualResetEvent to abort the sleep when time to shutdown.
                    Thread.sleep(consumerConfiguration.getDelayWhenNoAssignment().toMillis());
                    continue;
                }
                runner.run();
            }

            try {
                runnerProvider.close();
            } catch (final Exception e) {
                log.error("Error when closing runnerProvider", e);
            }

            log.info("Stopped ExecutorAsync. Consumer={0}, Worker Number={1} " + getWorkerStatus().getWorkerId().getConsumer().getConsumerId().getName(), getWorkerStatus().getWorkerId().getWorkerNumber());
            getServiceStatus().stopped();
        } catch (final Exception e2) {
            log.error("Error in run", e2);
        }
    }

    @Override
    public void start() {
        getServiceStatus().initialize(s -> s.getEventHandlers().onPostStarting(t -> {
            thread = new Thread(this::run);
            thread.start();
        }));
        super.start();
    }

    @Override
    public void close() throws Exception {
        super.close();
        if (thread != null) {
            thread = null;
        }
        runnerProvider.close();
    }
}
