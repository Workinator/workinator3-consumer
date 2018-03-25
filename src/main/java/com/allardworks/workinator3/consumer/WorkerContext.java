package com.allardworks.workinator3.consumer;

public interface WorkerContext {
    boolean canContinue();
    Assignment getAssignment();
    void hasWork(boolean hasWork);
    boolean hasWork();
}
