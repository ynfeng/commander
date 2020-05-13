package com.github.ynfeng.commander.core.executor;

import com.github.ynfeng.commander.core.context.ProcessContext;
import com.github.ynfeng.commander.core.definition.NodeDefinition;
import com.github.ynfeng.commander.core.definition.ServiceDefinition;

public class ServiceNodeExecutor implements NodeExecutor {

    @Override
    public void execute(ProcessContext context) {
        ServiceDefinition serviceDefinition = context.readyNode();
        context.completeReadyNode(serviceDefinition.next());
    }

    @Override
    public boolean canExecute(NodeDefinition nodeDefinition) {
        return nodeDefinition instanceof ServiceDefinition;
    }
}
