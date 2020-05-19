package com.github.ynfeng.commander.core.engine.executor;

import com.github.ynfeng.commander.core.context.ProcessContext;
import com.github.ynfeng.commander.core.definition.EndDefinition;
import com.github.ynfeng.commander.core.definition.NodeDefinition;

public class EndNodeExecutor implements NodeExecutor {

    @Override
    public void execute(ProcessContext context, NodeDefinition nodeDefinition) {
        context.completeReadyNode(NodeDefinition.NULL);
        context.done();
    }

    @Override
    public boolean canExecute(NodeDefinition nodeDefinition) {
        return nodeDefinition instanceof EndDefinition;
    }
}
