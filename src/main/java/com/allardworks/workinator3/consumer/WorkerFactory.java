package com.allardworks.workinator3.consumer;

/**
 * To be implemented by the application and provided as a bean.
 * Creates a worker for the given worker id.
 */
public interface WorkerFactory<T> {
    T createWorker(Assignment assignment);
}
