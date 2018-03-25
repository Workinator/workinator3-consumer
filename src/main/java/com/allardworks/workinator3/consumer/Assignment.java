package com.allardworks.workinator3.consumer;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Date;


@RequiredArgsConstructor
@Getter
public class Assignment {
    private final WorkerId workerId;
    private final String partitionKey;
    private final String receipt;
    private final String ruleName;
    private final Date assignmentDate;

    /**
     * Returns a new assignment object with all of the same information
     * except for rule name.
     * @param newRuleName
     * @return
     */
    public static Assignment setRule(final Assignment source, final String newRuleName) {
        return new Assignment(source.getWorkerId(), source.getPartitionKey(), source.getReceipt(), newRuleName, source.getAssignmentDate());
    }

    public static Assignment setWorkerId(final Assignment source, final WorkerId workerId) {
        return new Assignment(workerId, source.getPartitionKey(), source.getReceipt(), source.getRuleName(), source.getAssignmentDate());
    }
}
