package com.github.ynfeng.commander.core.context.event;

import com.github.ynfeng.commander.core.context.ProcessContext;

public class NodeExecuteCompletedEvent extends NodeExecuteEvent {

    protected NodeExecuteCompletedEvent(ProcessContext processContext) {
        super(processContext);
    }
}
