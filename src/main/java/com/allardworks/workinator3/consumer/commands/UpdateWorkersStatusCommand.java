package com.allardworks.workinator3.consumer.commands;

import com.allardworks.workinator3.consumer.WorkerStatus;
import lombok.Data;

import java.util.List;

@Data
public class UpdateWorkersStatusCommand {
    private final List<WorkerStatus> status;
}
