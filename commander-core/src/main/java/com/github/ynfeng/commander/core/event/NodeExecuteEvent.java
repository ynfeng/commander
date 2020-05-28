package com.github.ynfeng.commander.core.event;

import com.github.ynfeng.commander.core.context.ProcessContext;

public abstract class NodeExecuteEvent implements EngineEvent {
    private final ProcessContext processContext;

    protected NodeExecuteEvent(ProcessContext processContext) {
        this.processContext = processContext;
    }

    public ProcessContext processContext() {
        return processContext;
    }
}
