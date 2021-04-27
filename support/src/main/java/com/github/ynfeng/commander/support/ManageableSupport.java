package com.github.ynfeng.commander.support;

import java.util.concurrent.atomic.AtomicBoolean;

public abstract class ManageableSupport implements Manageable {
    private final AtomicBoolean isStarted = new AtomicBoolean();

    @Override
    public void start() {
        if (isStarted.compareAndSet(false, true)) {
            doStart();
        }
    }

    protected abstract void doStart();

    @Override
    public void shutdown() {
        if (isStarted.compareAndSet(true, false)) {
            doShutdown();
        }
    }

    protected abstract void doShutdown();

    @Override
    public boolean isStarted() {
        return isStarted.get();
    }

}
