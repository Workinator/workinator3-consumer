package com.allardworks.workinator3.consumer.commands;

import com.allardworks.workinator3.consumer.ConsumerRegistration;
import lombok.Data;

/**
 * Created by jaya on 2/27/18.
 * k?
 */
@Data
public class UnregisterConsumerCommand {
    private final ConsumerRegistration registration;
}
