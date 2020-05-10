package com.github.ynfeng.commander.core.executor;

import com.github.ynfeng.commander.core.context.ProcessContext;
import com.github.ynfeng.commander.core.definition.NodeDefinition;
import com.github.ynfeng.commander.core.engine.EngineContext;
import com.github.ynfeng.commander.core.event.NodeExecuteCompleteEvent;

public class NullNodeExecutor implements NodeExecutor {

    @Override
    public void execute(ProcessContext context) {
        context.start();
        EngineContext.publishEvent(NodeExecuteCompleteEvent.create(context));
    }

    @Override
    public boolean canExecute(NodeDefinition nodeDefinition) {
        return nodeDefinition == NodeDefinition.NULL;
    }
}
