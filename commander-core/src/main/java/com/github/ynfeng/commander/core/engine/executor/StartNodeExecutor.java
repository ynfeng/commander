package com.github.ynfeng.commander.core.engine.executor;

import com.github.ynfeng.commander.core.context.ProcessContext;
import com.github.ynfeng.commander.core.definition.NodeDefinition;
import com.github.ynfeng.commander.core.definition.StartDefinition;

public class StartNodeExecutor implements NodeExecutor {

    @Override
    public void execute(ProcessContext context, NodeDefinition nodeDefinition) {
        StartDefinition startDefinition = (StartDefinition) nodeDefinition;
        context.running();
        context.completeReadyNode(startDefinition.next());
    }

    @Override
    public boolean canExecute(NodeDefinition nodeDefinition) {
        return nodeDefinition instanceof StartDefinition;
    }
}
