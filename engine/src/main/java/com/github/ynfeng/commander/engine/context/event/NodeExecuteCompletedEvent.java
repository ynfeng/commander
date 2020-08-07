package com.github.ynfeng.commander.engine.context.event;

import com.github.ynfeng.commander.engine.context.ProcessContext;

public class NodeExecuteCompletedEvent extends NodeExecuteEvent {

    public NodeExecuteCompletedEvent(ProcessContext processContext) {
        super(processContext);
    }
}
