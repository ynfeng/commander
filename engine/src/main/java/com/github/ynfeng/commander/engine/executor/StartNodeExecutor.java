package com.github.ynfeng.commander.engine.executor;

import com.github.ynfeng.commander.definition.NodeDefinition;
import com.github.ynfeng.commander.definition.StartDefinition;
import com.github.ynfeng.commander.engine.context.ProcessContext;

public class StartNodeExecutor implements NodeExecutor {

    @Override
    public void execute(ProcessContext context, NodeDefinition nodeDefinition) {
        StartDefinition startDefinition = (StartDefinition) nodeDefinition;
        context.running();
        context.addReadyNode(startDefinition.next());
        context.completeNode(nodeDefinition);
    }

    @Override
    public boolean canExecute(NodeDefinition nodeDefinition) {
        return nodeDefinition instanceof StartDefinition;
    }
}
