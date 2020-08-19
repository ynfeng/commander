package com.github.ynfeng.commander.engine.executor;

import com.github.ynfeng.commander.definition.NodeDefinition;
import com.github.ynfeng.commander.definition.ServiceDefinition;
import com.github.ynfeng.commander.engine.context.ProcessContext;

public class ServiceNodeExecutor implements NodeExecutor {

    @Override
    public void execute(ProcessContext context, NodeDefinition nodeDefinition) {
        ServiceDefinition serviceDefinition = (ServiceDefinition) nodeDefinition;
        context.addReadyNode(serviceDefinition.next());
        context.completeNode(nodeDefinition);
    }

    @Override
    public boolean canExecute(NodeDefinition nodeDefinition) {
        return nodeDefinition instanceof ServiceDefinition;
    }
}
