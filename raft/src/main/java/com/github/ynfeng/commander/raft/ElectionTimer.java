package com.github.ynfeng.commander.raft;

import com.github.ynfeng.commander.support.ManageableSupport;
import java.time.Instant;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ElectionTimer extends ManageableSupport {
    private final ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1);
    private final Runnable timeoutAction;
    private final long timeout;
    private volatile long lastResetTime;

    public ElectionTimer(long timeout, Runnable timeoutAction) {
        this.timeoutAction = timeoutAction;
        this.timeout = timeout;
        lastResetTime = Instant.now().toEpochMilli();
    }

    private boolean isTimeout() {
        long now = Instant.now().toEpochMilli();
        long escapeTime = now - lastResetTime;
        return escapeTime > timeout;
    }

    public void reset() {
        lastResetTime = Instant.now().toEpochMilli();
    }

    @Override
    protected void doStart() {
        executor.scheduleWithFixedDelay(this::detectElectionTimeout,
            timeout,
            timeout,
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
