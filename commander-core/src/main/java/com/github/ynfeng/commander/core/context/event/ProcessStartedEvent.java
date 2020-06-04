package com.github.ynfeng.commander.core.context.event;

import com.github.ynfeng.commander.core.context.ProcessContext;

public class ProcessStartedEvent extends NodeExecuteEvent {

    public ProcessStartedEvent(ProcessContext processContext) {
        super(processContext);
    }
}
