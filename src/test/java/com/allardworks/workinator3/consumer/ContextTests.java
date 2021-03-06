package com.allardworks.workinator3.consumer;

import com.allardworks.workinator3.core.*;
import lombok.val;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertTrue;

/**
 * Created by jaya on 2/24/18.
 * k?
 */
public class ContextTests {
    /**
     * Mae sure elapsed time increases.
     * This is in response to a bug where the parameters were backwards in the context,
     * so elapsed time was decreasing.
     */
    @Test
    public void elapsed() throws InterruptedException {
        val consumerId = new ConsumerId("a");
        val consumerRegistration = new ConsumerRegistration(consumerId, "");
        val workerId = new WorkerId(consumerRegistration, 1);
        val assignment = new Assignment(workerId, "a", "", "", new Date());
        val context = new Context(assignment, c -> true);

        Thread.sleep(3);
        assertTrue(context.getElapsed().toMillis() > 3);
    }
}
