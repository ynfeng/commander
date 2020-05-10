package com.github.ynfeng.commander.core.engine;

import com.github.ynfeng.commander.core.event.EngineEvent;
import com.github.ynfeng.commander.core.event.ProcessEngineEventBus;

public final class EngineContext {
    private final ProcessEngineEventBus eventBus = new ProcessEngineEventBus();
    private static final EngineContext INSTANCE = new EngineContext();

    private EngineContext() {

    }

    public static void registerListener(ExecutorLauncher executorLauncher) {
        INSTANCE.eventBus.registerListener(executorLauncher);
    }

    public static void publishEvent(EngineEvent engineEvent) {
        INSTANCE.eventBus.publishEvent(engineEvent);
    }
}
