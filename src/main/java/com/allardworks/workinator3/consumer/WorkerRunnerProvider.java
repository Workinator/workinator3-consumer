package com.allardworks.workinator3.consumer;

import com.allardworks.workinator3.core.Assignment;
import com.allardworks.workinator3.core.ServiceStatus;
import com.allardworks.workinator3.core.WorkerStatus;
import com.allardworks.workinator3.core.Workinator;
import lombok.RequiredArgsConstructor;
import lombok.val;

import java.util.function.Function;

/**
 * Manages workers runners.
 * The executors need runners. Runners are based on assignments.
 * This creates/closes runners as assignments changes.
 * If the assignment doesn't change, then it will return the existing runner.
 * If there isn't an assignment, then it returns a null.
 */
@RequiredArgsConstructor
class WorkerRunnerProvider implements AutoCloseable {
    private final Function<Context, Boolean> canContinue;
    private final AsyncWorkerFactory workerFactory;
    private final Workinator workinator;
    private final WorkerStatus workerStatus;
    private final ServiceStatus serviceStatus;
    private WorkerRunner current;

    /**
     * Terminate the existing runner, if there is one.
     */
    private void closeCurrent() {
        if (current == null) {
            return;
        }
        current.close();
        current = null;
    }

    /**
     * Creates a new WorkerRunner.
     *
     * @param newAssignment
     * @return
     */
    private WorkerRunner createWorkerRunner(final Assignment newAssignment) {
        val worker = workerFactory.createWorker(newAssignment);
        val context = new Context(newAssignment, workerStatus, () ->serviceStatus, canContinue);
        return new WorkerRunner(workinator, workerStatus, worker, context);
    }

    /**
     * Returns the current assignment.
     *
     * @return
     */
    public Assignment getCurrentAssignment() {
        val c = current;
        if (c == null) {
            return null;
        }

        return c.getStatus().getCurrentAssignment();
    }

    /**
     * Returns the current run context.
     *
     * @return
     */
    public WorkerContext getCurrentContext() {
        val c = current;
        if (c == null) {
            return null;
        }

        return c.getContext();
    }

    /**
     * Creates a new WorkerRunner, or returns the existing one.
     * - if there isn't an assignment for this worker, then returns null.
     * - if there's a new assignment, closes the old runner and returns a new one.
     * - if the assignment doesn't change, then returns the current runner.
     *
     * @return
     */
    public WorkerRunner lookupRunner() {
        val newAssignment = workinator.getAssignment(workerStatus);
        val me = workerStatus.getWorkerId().getConsumer().getConsumerId().getName() + "." + workerStatus.getWorkerId().getWorkerNumber() + ": ";

        // no assignment. close the current, if there is one.
        if (newAssignment == null) {
            //System.out.println(me + "new assignment: null");
            closeCurrent();
            return null;
        }

        // new assignment, no old assignment.
        // setup the new assignment.
        if (current == null || current.getStatus().getCurrentAssignment() == null) {
            //System.out.println(me + "new assignment, no old: " + newAssignment.getPartitionKey());
            current = createWorkerRunner(newAssignment);
            workerStatus.setCurrentAssignment(newAssignment);
            return current;
        }

        // old assignment and new assignment are the same. nothing to do.
        if (current.getStatus().getCurrentAssignment().getPartitionKey().equals(newAssignment.getPartitionKey())) {
            //System.out.println(me + "no change: " + current.getStatus().getCurrentAssignment().getPartitionKey());
            return current;
        }

        // new assignment is different than the old.
        // close the old. setup the new.
        //System.out.println(me + ": replacement: old=" + current.getStatus().getCurrentAssignment().getPartitionKey() + ", new=" + newAssignment.getPartitionKey());
        closeCurrent();
        current = createWorkerRunner(newAssignment);
        workerStatus.setCurrentAssignment(newAssignment);
        return current;
    }

    /**
     * Terminate the current worker runner.
     *
     * @throws Exception
     */
    @Override
    public void close() {
        closeCurrent();
    }
}