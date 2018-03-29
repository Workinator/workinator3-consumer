package com.allardworks.workinator3.consumer;

import com.allardworks.workinator3.core.Assignment;
import com.allardworks.workinator3.core.Status;
import com.allardworks.workinator3.core.WorkerId;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ExecutorInfo {
    private boolean hasWork;
    private Assignment assignment;
    private WorkerId workerId;
    private String executorType;
    private Status status;
}
