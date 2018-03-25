package com.allardworks.workinator3.consumer;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ConsumerExistsException extends Exception {
    private final String consumerId;
    public ConsumerExistsException(final String consumerId) {
        super("The consumer already exists: " + consumerId);
        this.consumerId = consumerId;
    }
}
