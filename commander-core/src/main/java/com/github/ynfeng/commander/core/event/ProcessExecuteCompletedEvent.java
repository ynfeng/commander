package com.github.ynfeng.commander.core.event;

import com.github.ynfeng.commander.core.context.ProcessContext;

public class ProcessExecuteCompletedEvent extends NodeExecuteEvent {

    protected ProcessExecuteCompletedEvent(ProcessContext processContext) {
        super(processContext);
    }
}
