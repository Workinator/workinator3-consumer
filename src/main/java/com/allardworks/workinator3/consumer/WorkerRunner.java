package com.allardworks.workinator3.consumer;

import com.allardworks.workinator3.core.WorkerStatus;
import com.allardworks.workinator3.core.Workinator;
import com.allardworks.workinator3.core.commands.ReleaseAssignmentCommand;
import com.allardworks.workinator3.core.commands.SetPartitionStatusCommand;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;

/**
 * Runs the worker until it is time for the worker to stop.
 */
@AllArgsConstructor
@Getter
@Slf4j
class WorkerRunner {
    private final Workinator workinator;
    private final WorkerStatus status;
    private final AsyncWorker worker;
    private WorkerContext context;

    /**
     * A loop that executes the worker until
     * - workinator says stop
     * - or, the worker reports there isn't any more work.
     */
    void run() {
        while (context.canContinue()) {
            try {
                worker.execute(context);

                // TODO: hack. need an event stream to be consumed elsewhere.
                // too many updates happening here.... will remove.
                workinator.setPartitionStatus(
                        SetPartitionStatusCommand
                        .builder()
                        .partitionKey(context.getAssignment().getPartitionKey())
                        .hasWork(context.hasWork())
                        .build());
                if (!context.hasWork()) {
                    break;
                }

                //TODO: need to slow it down... possibly temporary
                Thread.sleep(100);
            } catch (final Exception ex) {
                log.error("worker.execute", ex);
            }
        }
    }


    public void hackSetContext(Context context) {
        this.context = context;
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

        workinator.releaseAssignment(new ReleaseAssignmentCommand(status.getCurrentAssignment()));
    }
}
