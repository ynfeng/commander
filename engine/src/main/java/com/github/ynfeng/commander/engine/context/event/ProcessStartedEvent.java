package com.github.ynfeng.commander.engine.context.event;

import com.github.ynfeng.commander.engine.context.ProcessContext;

public class ProcessStartedEvent extends NodeExecuteEvent {

    public ProcessStartedEvent(ProcessContext processContext) {
        super(processContext);
    }
}
