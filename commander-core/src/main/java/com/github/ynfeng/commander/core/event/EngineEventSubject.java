package com.github.ynfeng.commander.core.event;

import com.google.common.eventbus.EventBus;
import java.util.concurrent.atomic.AtomicInteger;

public class EngineEventSubject {
    private static final EngineEventSubject INSTANCE = new EngineEventSubject();
    private final AtomicInteger numOfListener = new AtomicInteger(0);
    private EventBus eventBus = new EventBus();

    private EngineEventSubject() {

    }

    public static EngineEventSubject getInstance() {
        return INSTANCE;
    }

    public void registerListener(Object listener) {
        eventBus.register(listener);
        numOfListener.getAndIncrement();
    }

    public void removeAllListeners() {
        eventBus = new EventBus();
        numOfListener.set(0);
    }

    public void removeListener(Object eventListener) {
        eventBus.unregister(eventListener);
        numOfListener.decrementAndGet();
    }

    public int numOfListeners() {
        return numOfListener.get();
    }

    public void notifyProcessExecutedComplete() {
        eventBus.post(new ProcessExecuteCompletedEvent());
    }

    public void notifyNodeExecutedComplete() {
        eventBus.post(new NodeExecuteCompletedEvent());
    }

    public void notifyProcessStartedEvent() {
        eventBus.post(new ProcessStartedEvent());
    }
}
