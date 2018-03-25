package com.allardworks.workinator3.consumer;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@EqualsAndHashCode
@Getter
@Component
public class ConsumerId {
    public ConsumerId(@Value("#{ '${name:}' ?: T(com.allardworks.workinator3.core.NameUtility).randomName }") String name) {
        this.name=name;
    }

    private final String name;
}
