package com.allardworks.workinator3.consumer;

public interface AsyncWorker extends Worker {
    void execute(WorkerContext context);
}
