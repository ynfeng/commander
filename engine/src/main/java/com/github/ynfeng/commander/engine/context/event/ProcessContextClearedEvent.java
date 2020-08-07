package com.github.ynfeng.commander.engine.context.event;

import com.github.ynfeng.commander.engine.context.ProcessContext;

public class ProcessContextClearedEvent extends NodeExecuteEvent {

    public ProcessContextClearedEvent(ProcessContext processContext) {
        super(processContext);
    }
}
