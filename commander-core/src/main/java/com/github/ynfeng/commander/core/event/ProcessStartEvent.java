package com.github.ynfeng.commander.core.event;

import com.github.ynfeng.commander.core.context.ProcessContext;

public class ProcessStartEvent extends AbstractEngineEvent {

    private ProcessStartEvent(ProcessContext processContext) {
        super(processContext);
    }

    public static ProcessStartEvent create(ProcessContext processContext) {
        return new ProcessStartEvent(processContext);
    }
}
