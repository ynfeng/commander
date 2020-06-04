package com.github.ynfeng.commander.core.context.event;

import com.github.ynfeng.commander.core.context.ProcessContext;

public class ProcessExecuteCompletedEvent extends NodeExecuteEvent {

    public ProcessExecuteCompletedEvent(ProcessContext processContext) {
        super(processContext);
    }
}
