package com.github.ynfeng.commander.core.context.event;


import com.github.ynfeng.commander.core.context.ProcessContext;

public class ProcessExecuteFailedEvent extends NodeExecuteEvent {
    protected ProcessExecuteFailedEvent(ProcessContext processContext) {
        super(processContext);
    }
}
