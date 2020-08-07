package com.github.ynfeng.commander.engine.context.event;


import com.github.ynfeng.commander.engine.context.ProcessContext;

public class ProcessExecuteFailedEvent extends NodeExecuteEvent {
    public ProcessExecuteFailedEvent(ProcessContext processContext) {
        super(processContext);
    }
}
