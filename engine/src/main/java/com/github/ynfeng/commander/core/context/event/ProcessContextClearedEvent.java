package com.github.ynfeng.commander.core.context.event;

import com.github.ynfeng.commander.core.context.ProcessContext;

public class ProcessContextClearedEvent extends NodeExecuteEvent {

    public ProcessContextClearedEvent(ProcessContext processContext) {
        super(processContext);
    }
}
