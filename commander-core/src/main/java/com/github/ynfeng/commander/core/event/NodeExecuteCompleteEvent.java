package com.github.ynfeng.commander.core.event;

import com.github.ynfeng.commander.core.context.ProcessContext;

public class NodeExecuteCompleteEvent implements EngineEvent {
    private ProcessContext processContext;

    public NodeExecuteCompleteEvent(ProcessContext processContext) {
        this.processContext = processContext;
    }

    @Override
    public ProcessContext context() {
        return processContext;
    }

    public static NodeExecuteCompleteEvent create(ProcessContext processContext) {
        return new NodeExecuteCompleteEvent(processContext);
    }
}
