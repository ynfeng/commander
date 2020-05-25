package com.github.ynfeng.commander.core.event;

public class EngineEventSubject {
    private static final EngineEventSubject INSTANCE = new EngineEventSubject();
    private static final EventBus EVENT_BUS = new ProcessEngineEventBus();

    public static EngineEventSubject getInstance() {
        return INSTANCE;
    }

    public void registerListener(EventListener listener) {
        EVENT_BUS.registerListener(listener);
    }

    public void removeAllListeners() {
        EVENT_BUS.removeAllListeners();
    }

    public void removeListener(EventListener eventListener) {
        EVENT_BUS.removeListener(eventListener);
    }

    public int numOfListeners() {
        return EVENT_BUS.numOfListeners();
    }

    public void notifyProcessExecutedComplete() {
        EVENT_BUS.publishEvent(new ProcessExecuteCompletedEvent());
    }

    public void notifyNodeExecutedComplete() {
        EVENT_BUS.publishEvent(new NodeExecuteCompletedEvent());
    }

    public void notifyProcessStartedEvent() {
        EVENT_BUS.publishEvent(new ProcessStartedEvent());
    }
}
