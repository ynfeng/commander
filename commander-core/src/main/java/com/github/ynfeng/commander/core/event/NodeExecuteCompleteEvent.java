package com.github.ynfeng.commander.core.event;

import com.github.ynfeng.commander.core.context.ProcessContext;

public class NodeExecuteCompleteEvent extends AbstractEngineEvent {

    private NodeExecuteCompleteEvent(ProcessContext processContext) {
        super(processContext);
    }

    public static NodeExecuteCompleteEvent create(ProcessContext processContext) {
        return new NodeExecuteCompleteEvent(processContext);
    }
}
