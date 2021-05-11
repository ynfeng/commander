package com.github.ynfeng.commander.raft;

import com.github.ynfeng.commander.support.ManageableSupport;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import java.time.Instant;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class ElectionTimer extends ManageableSupport {
    private final ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1,
        new ThreadFactoryBuilder().setNameFormat("election-timer-thread-%d").build());
    private final Runnable timeoutAction;
    private final long timeout;
    private volatile long lastResetTime;
    private volatile boolean pause;

    public ElectionTimer(long timeout, Runnable timeoutAction) {
        this.timeoutAction = timeoutAction;
        this.timeout = timeout;
        lastResetTime = Instant.now().toEpochMilli();
    }

    private boolean isTimeout() {
        if (pause) {
            return false;
        }
        long now = Instant.now().toEpochMilli();
        long escapeTime = now - lastResetTime;
        return escapeTime > timeout;
    }

    public void reset() {
        long randomMs = ThreadLocalRandom.current().nextLong(150, 300);
        lastResetTime = Instant.now().toEpochMilli() - randomMs;
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

    public void pause() {
        pause = true;
    }

    public void resume() {
        pause = false;
    }
}
