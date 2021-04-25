package com.github.ynfeng.commander.raft;

import com.github.ynfeng.commander.support.ManageableSupport;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ElectionTimeoutDetector extends ManageableSupport {
    private final ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1);
    private final long electionTimeoutDetectionInterval;
    private final Runnable timeoutAction;

    public ElectionTimeoutDetector(long electionTimeoutDetectionInterval, Runnable timeoutAction) {
        this.electionTimeoutDetectionInterval = electionTimeoutDetectionInterval;
        this.timeoutAction = timeoutAction;
    }

    private boolean isTimeout() {
        return true;
    }

    @Override
    protected void doStart() {
        executor.scheduleWithFixedDelay(this::detectElectionTimeout,
            electionTimeoutDetectionInterval,
            electionTimeoutDetectionInterval,
            TimeUnit.MILLISECONDS);
    }

    private void detectElectionTimeout() {
        if (isTimeout()) {
            timeoutAction.run();
        }
    }

    @Override
    protected void doShutdown() {
        executor.shutdownNow();
    }
}
