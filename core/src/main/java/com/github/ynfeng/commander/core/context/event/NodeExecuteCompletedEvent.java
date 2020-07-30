package com.github.ynfeng.commander.core.context.event;

import com.github.ynfeng.commander.core.context.ProcessContext;

public class NodeExecuteCompletedEvent extends NodeExecuteEvent {

    public NodeExecuteCompletedEvent(ProcessContext processContext) {
        super(processContext);
    }
}
