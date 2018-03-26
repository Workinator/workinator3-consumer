package com.allardworks.workinator3.consumer;

import com.allardworks.workinator3.core.Assignment;

public interface WorkerContext {
    boolean canContinue();
    Assignment getAssignment();
    void hasWork(boolean hasWork);
    boolean hasWork();
}
