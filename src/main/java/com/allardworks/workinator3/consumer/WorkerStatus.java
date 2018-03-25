package com.allardworks.workinator3.consumer;

import lombok.Data;
import lombok.val;

@Data
public class WorkerStatus {
    private final WorkerId workerId;

    private boolean hasWork;

    private Assignment currentAssignment;

    public WorkerStatus clone() {
        val copy = new WorkerStatus(workerId);
        copy.hasWork = hasWork;
        copy.currentAssignment = currentAssignment;
        return copy;
    }
}
