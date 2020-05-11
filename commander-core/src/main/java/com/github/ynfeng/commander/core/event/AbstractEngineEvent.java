package com.github.ynfeng.commander.core.event;

import com.github.ynfeng.commander.core.context.ProcessContext;

public class AbstractEngineEvent implements EngineEvent {
    protected ProcessContext processContext;

    public AbstractEngineEvent(ProcessContext processContext) {
        this.processContext = processContext;
    }

    @Override
    public ProcessContext context() {
        return processContext;
    }
}
