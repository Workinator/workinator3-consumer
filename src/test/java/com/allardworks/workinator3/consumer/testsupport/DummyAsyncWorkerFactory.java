package com.allardworks.workinator3.consumer.testsupport;

import com.allardworks.workinator3.consumer.AsyncWorker;
import com.allardworks.workinator3.consumer.AsyncWorkerFactory;
import com.allardworks.workinator3.core.Assignment;
import lombok.RequiredArgsConstructor;

import java.util.function.Supplier;

@RequiredArgsConstructor
public class DummyAsyncWorkerFactory implements AsyncWorkerFactory {
    private final Supplier<AsyncWorker> supplier;

    @Override
    public AsyncWorker createWorker(Assignment assignment) {
        return supplier.get();
    }
}
