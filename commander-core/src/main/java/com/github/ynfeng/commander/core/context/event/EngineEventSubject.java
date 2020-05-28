package com.github.ynfeng.commander.core.context.event;

import com.github.ynfeng.commander.core.context.ProcessContext;
import com.google.common.eventbus.EventBus;
import java.util.concurrent.atomic.AtomicInteger;

@SuppressWarnings("UnstableApiUsage")
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

    public void notifyProcessExecutedComplete(ProcessContext context) {
        eventBus.post(new ProcessExecuteCompletedEvent(context));
    }

    public void notifyNodeExecutedComplete(ProcessContext context) {
        eventBus.post(new NodeExecuteCompletedEvent(context));
    }

    public void notifyProcessStartedEvent(ProcessContext context) {
        eventBus.post(new ProcessStartedEvent(context));
    }

    public void notifyProcessContextCleared(ProcessContext context) {
        eventBus.post(new ProcessContextClearedEvent(context));
    }

    public void notifyProcessExecutedException(ProcessContext context) {
        eventBus.post(new ProcessExecuteFailedEvent(context));
    }
}
