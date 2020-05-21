package com.github.ynfeng.commander.core.context;

import com.github.ynfeng.commander.core.event.EngineEvent;
import com.github.ynfeng.commander.core.event.EventListener;
import com.github.ynfeng.commander.core.event.ProcessEngineEventBus;

public final class EngineContext {
    private final ProcessEngineEventBus eventBus = new ProcessEngineEventBus();
    private static final EngineContext INSTANCE = new EngineContext();

    private EngineContext() {

    }

    public static void registerEventListener(EventListener eventListener) {
        INSTANCE.eventBus.registerListener(eventListener);
    }

    public static void publishEvent(EngineEvent engineEvent) {
        INSTANCE.eventBus.publishEvent(engineEvent);
    }

    public static void destory() {
        INSTANCE.eventBus.removeAllListeners();
    }

    public static void removeEventListener(EventListener eventListener) {
        INSTANCE.eventBus.removeListener(eventListener);
    }

    public static int numOfEventListeners() {
        return INSTANCE.eventBus.numOfListeners();
    }
}
