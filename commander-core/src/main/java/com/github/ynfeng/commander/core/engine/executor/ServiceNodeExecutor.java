package com.github.ynfeng.commander.core.engine.executor;

import com.github.ynfeng.commander.core.context.ProcessContext;
import com.github.ynfeng.commander.core.definition.NodeDefinition;
import com.github.ynfeng.commander.core.definition.ServiceDefinition;

public class ServiceNodeExecutor implements NodeExecutor {

    @Override
    public void execute(ProcessContext context, NodeDefinition nodeDefinition) {
        ServiceDefinition serviceDefinition = (ServiceDefinition) nodeDefinition;
        context.completeReadyNode(serviceDefinition.next());
    }

    @Override
    public boolean canExecute(NodeDefinition nodeDefinition) {
        return nodeDefinition instanceof ServiceDefinition;
    }
}
