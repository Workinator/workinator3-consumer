package com.allardworks.workinator3.consumer;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * Created by jaya on 3/5/18.
 * k?
 */
@Data
@Builder
public class ConsumerStatus {
    private List<ConsumerWorkerStatus> workers;

    @Builder
    @Data
    public static class ConsumerWorkerStatus {
        private int workerNumber;
        private Assignment assignment;
    }
}
