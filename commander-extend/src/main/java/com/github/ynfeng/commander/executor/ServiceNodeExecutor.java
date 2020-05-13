package com.github.ynfeng.commander.executor;

import com.github.ynfeng.commander.core.context.ProcessContext;
import com.github.ynfeng.commander.core.definition.NodeDefinition;
import com.github.ynfeng.commander.core.executor.NodeExecutor;
import com.github.ynfeng.commander.definition.ServiceDefinition;

public class ServiceNodeExecutor implements NodeExecutor {

    @Override
    public void execute(ProcessContext context) {
        ServiceDefinition serviceDefinition = context.currentNode();
        context.completeCurrentNode(serviceDefinition.next());
    }

    @Override
    public boolean canExecute(NodeDefinition nodeDefinition) {
        return nodeDefinition instanceof ServiceDefinition;
    }
}
