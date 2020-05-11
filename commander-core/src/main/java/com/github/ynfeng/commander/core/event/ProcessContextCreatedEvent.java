package com.github.ynfeng.commander.core.event;

import com.github.ynfeng.commander.core.context.ProcessContext;

public class ProcessContextCreatedEvent extends AbstractEngineEvent {

    private ProcessContextCreatedEvent(ProcessContext processContext) {
        super(processContext);
    }

    public static ProcessContextCreatedEvent create(ProcessContext processContext) {
        return new ProcessContextCreatedEvent(processContext);
    }
}
