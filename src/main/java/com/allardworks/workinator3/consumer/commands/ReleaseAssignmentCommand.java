package com.allardworks.workinator3.consumer.commands;

import com.allardworks.workinator3.consumer.Assignment;
import lombok.Data;

@Data
public class ReleaseAssignmentCommand {
    private final Assignment assignment;
}
