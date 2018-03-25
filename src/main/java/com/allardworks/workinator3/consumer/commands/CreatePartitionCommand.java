package com.allardworks.workinator3.consumer.commands;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreatePartitionCommand {
    private String partitionKey;
    private int maxIdleTimeSeconds;
    private int maxWorkerCount;

    public static class CreatePartitionCommandBuilder {
        private int maxIdleTimeSeconds = 5;
        private int maxWorkerCount = 1;
    }
}
