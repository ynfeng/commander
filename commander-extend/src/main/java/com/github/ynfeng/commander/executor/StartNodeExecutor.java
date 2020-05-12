package com.github.ynfeng.commander.executor;

import com.github.ynfeng.commander.core.context.ProcessContext;
import com.github.ynfeng.commander.core.definition.NodeDefinition;
import com.github.ynfeng.commander.core.executor.NodeExecutor;
import com.github.ynfeng.commander.definition.StartDefinition;

public class StartNodeExecutor implements NodeExecutor {

    @Override
    public void execute(ProcessContext context) {
        StartDefinition startDefinition = context.currentNode();
        context.running();
        context.completeCurrentNode(startDefinition.next());
    }

    @Override
    public boolean canExecute(NodeDefinition nodeDefinition) {
        return nodeDefinition instanceof StartDefinition;
    }
}
