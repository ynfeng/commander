package com.github.ynfeng.commander.core.executor;

import com.github.ynfeng.commander.core.context.ProcessContext;
import com.github.ynfeng.commander.core.definition.NodeDefinition;
import com.github.ynfeng.commander.core.definition.StartDefinition;

public class StartNodeExecutor implements NodeExecutor {

    @Override
    public void execute(NodeDefinition nodeDefinition) {
        StartDefinition startDefinition = (StartDefinition) nodeDefinition;
        ProcessContext context = ProcessContext.get();
        context.running();
        context.addReadyNode(startDefinition.next());
        context.completeNode(nodeDefinition);
    }

    @Override
    public boolean canExecute(NodeDefinition nodeDefinition) {
        return nodeDefinition instanceof StartDefinition;
    }
}
