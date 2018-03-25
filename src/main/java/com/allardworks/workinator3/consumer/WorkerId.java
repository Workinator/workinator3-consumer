package com.allardworks.workinator3.consumer;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode
@Data
public class WorkerId {
    private final ConsumerRegistration consumer;
    private final int workerNumber;

    public String getAssignee() {
        return consumer.getConsumerId().getName() + ", #" + workerNumber;
    }
}
