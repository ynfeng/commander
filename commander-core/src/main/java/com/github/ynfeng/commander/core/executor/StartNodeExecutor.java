package com.github.ynfeng.commander.core.executor;

import com.github.ynfeng.commander.core.context.ProcessContext;
import com.github.ynfeng.commander.core.definition.NodeDefinition;
import com.github.ynfeng.commander.core.definition.StartDefinition;

public class StartNodeExecutor implements NodeExecutor {

    @Override
    public void execute(ProcessContext context) {
        StartDefinition startDefinition = context.readyNode();
        context.running();
        context.completeReadyNode(startDefinition.next());
    }

    @Override
    public boolean canExecute(NodeDefinition nodeDefinition) {
        return nodeDefinition instanceof StartDefinition;
    }
}
