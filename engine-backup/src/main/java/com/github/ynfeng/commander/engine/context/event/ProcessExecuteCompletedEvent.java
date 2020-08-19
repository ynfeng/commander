package com.github.ynfeng.commander.engine.context.event;

import com.github.ynfeng.commander.engine.context.ProcessContext;

public class ProcessExecuteCompletedEvent extends NodeExecuteEvent {

    public ProcessExecuteCompletedEvent(ProcessContext processContext) {
        super(processContext);
    }
}
