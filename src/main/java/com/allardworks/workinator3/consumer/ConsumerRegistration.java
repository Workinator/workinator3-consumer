package com.allardworks.workinator3.consumer;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode
@Data
public class ConsumerRegistration {
    private final ConsumerId consumerId;
    private final String receipt;
}
