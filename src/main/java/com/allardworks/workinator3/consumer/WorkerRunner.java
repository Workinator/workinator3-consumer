package com.allardworks.workinator3.consumer;

import com.allardworks.workinator3.core.WorkerStatus;
import com.allardworks.workinator3.core.Workinator;
import com.allardworks.workinator3.core.commands.ReleaseAssignmentCommand;
import com.allardworks.workinator3.core.commands.UpdateWorkersStatusCommand;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;

/**
 * Runs the worker until it is time for the worker to stop.
 */
@RequiredArgsConstructor
@Getter
@Slf4j
class WorkerRunner {
    private final Workinator workinator;
    private final WorkerStatus status;
    private final AsyncWorker worker;
    private final WorkerContext context;

    /**
     * A loop that executes the worker until
     * - workinator says stop
     * - or, the worker reports there isn't any more work.
     */
    void run() {
        while (context.canContinue()) {
            try {
                worker.execute(context);
                if (!status.isHasWork()) {
                    break;
                }
            } catch (final Exception ex) {
                log.error("worker.execute", ex);
            }
        }
    }


    /**
     * Terminate the worker.
     */
    void close(){
        try {
            worker.close();
        } catch (final Exception e) {
            log.error("Error closing worker", e);
        }

        workinator.updateWorkerStatus(new UpdateWorkersStatusCommand(Collections.singletonList(status)));
        workinator.releaseAssignment(new ReleaseAssignmentCommand(status.getCurrentAssignment()));
    }
}
