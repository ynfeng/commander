package com.github.ynfeng.commander.core.executor;

import com.github.ynfeng.commander.core.context.ProcessContext;
import com.github.ynfeng.commander.core.definition.EndDefinition;
import com.github.ynfeng.commander.core.definition.NodeDefinition;

public class EndNodeExecutor implements NodeExecutor {

    @Override
    public void execute(NodeDefinition nodeDefinition) {
        ProcessContext context = ProcessContext.get();
        context.addReadyNode(NodeDefinition.NULL);
        context.completeNode(nodeDefinition);
        context.complete();
    }

    @Override
    public boolean canExecute(NodeDefinition nodeDefinition) {
        return nodeDefinition instanceof EndDefinition;
    }
}
