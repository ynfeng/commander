package com.github.ynfeng.commander.executor;

import com.github.ynfeng.commander.core.context.ProcessContext;
import com.github.ynfeng.commander.core.definition.NodeDefinition;
import com.github.ynfeng.commander.core.engine.EngineContext;
import com.github.ynfeng.commander.core.event.NodeExecuteCompleteEvent;
import com.github.ynfeng.commander.core.event.ProcessExecuteCompleteEvent;
import com.github.ynfeng.commander.core.executor.NodeExecutor;
import com.github.ynfeng.commander.definition.EndDefinition;

public class EndNodeExecutor implements NodeExecutor {

    @Override
    public void execute(ProcessContext context) {
        context.complete();
        EngineContext.publishEvent(NodeExecuteCompleteEvent.create(context));
        EngineContext.publishEvent(ProcessExecuteCompleteEvent.create(context));
    }

    @Override
    public boolean canExecute(NodeDefinition nodeDefinition) {
        return nodeDefinition instanceof EndDefinition;
    }
}
