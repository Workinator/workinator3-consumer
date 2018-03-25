package com.allardworks.workinator3.consumer;

import com.allardworks.workinator3.core.ServiceBase;
import lombok.RequiredArgsConstructor;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

/**
 * Created by jaya on 2/26/18.
 * k?
 */
@RequiredArgsConstructor
public class ScheduledTaskThread extends ServiceBase {
    private final Duration interval;
    private final Runnable action;
    private final CountDownLatch block = new CountDownLatch(1);
    private Thread thread;

    @Override
    public Map<String, Object> getInfo() {
        return null;
    }

    @Override
    public void start() {
        getServiceStatus().initialize(s -> {
            s.getEventHandlers().onPostStarting(t -> {
                thread = new Thread(this::run);
                thread.start();
            });

            // the RUN loop blocks on the countdown latch.
            // release the block when stopping.
            s.getEventHandlers().onPostStopping(t -> block.countDown());
        });
        super.start();
    }


    private void run() {
        getServiceStatus().started();
        while (getServiceStatus().getStatus().isStarted()) {
            action.run();

            try {
                // basically a sleep. it releases
                // every 10 seconds, or when the block is released.
                // the block is released when the service stops.
                block.await(interval.toMillis(), MILLISECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        getServiceStatus().stopped();
    }
}
