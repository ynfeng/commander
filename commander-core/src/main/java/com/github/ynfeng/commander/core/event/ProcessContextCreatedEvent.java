package com.github.ynfeng.commander.core.event;

import com.github.ynfeng.commander.core.context.ProcessContext;

public class ProcessContextCreatedEvent implements EngineEvent {
    private final ProcessContext processContext;

    private ProcessContextCreatedEvent(ProcessContext processContext) {
        this.processContext = processContext;
    }

    public static ProcessContextCreatedEvent create(ProcessContext processContext) {
        return new ProcessContextCreatedEvent(processContext);
    }

    public ProcessContext context() {
        return processContext;
    }
}
