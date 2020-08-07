package com.github.ynfeng.commander.engine.context.event;

import com.github.ynfeng.commander.engine.context.ProcessContext;
import com.github.ynfeng.commander.engine.event.EngineEvent;

public abstract class NodeExecuteEvent implements EngineEvent {
    private final ProcessContext processContext;

    protected NodeExecuteEvent(ProcessContext processContext) {
        this.processContext = processContext;
    }

    public ProcessContext processContext() {
        return processContext;
    }
}
