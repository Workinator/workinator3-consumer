package com.allardworks.workinator3.consumer;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ConsumerDoesntExistsException extends Exception {
    private final String consumerId;
    public ConsumerDoesntExistsException(final String consumerId) {
        super("The consumer already exists: " + consumerId);
        this.consumerId = consumerId;
    }
}
