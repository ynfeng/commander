package com.github.ynfeng.commander.core.event;

import com.github.ynfeng.commander.core.context.ProcessContext;

public class ProcessExecuteCompleteEvent extends AbstractEngineEvent {
    protected ProcessExecuteCompleteEvent(ProcessContext processContext) {
        super(processContext);
    }

    public static ProcessExecuteCompleteEvent create(ProcessContext processContext) {
        return new ProcessExecuteCompleteEvent(processContext);
    }
}
