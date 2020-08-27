package com.github.ynfeng.commander.engine.executor;

import com.github.ynfeng.commander.definition.NodeDefinition;
import com.github.ynfeng.commander.definition.ServiceDefinition;
import com.github.ynfeng.commander.engine.ProcessInstance;
import java.util.concurrent.CompletableFuture;

public class ServiceNodeExecutor implements NodeExecutor {

    @Override
    public void execute(ProcessInstance processInstance, NodeDefinition nodeDefinition) {
        ServiceDefinition serviceDefinition = (ServiceDefinition) nodeDefinition;
        CompletableFuture<NodeExecutingVariable> variableFuture =
            processInstance.getNodeExecutingVariable(nodeDefinition.refName());
        variableFuture.thenAccept(variable -> {
            ServiceNodeExecuteState state = variable.get("state", ServiceNodeExecuteState.Created);
            state.accept(processInstance, serviceDefinition);
        });
    }

    @Override
    public boolean canExecute(NodeDefinition nodeDefinition) {
        return nodeDefinition instanceof ServiceDefinition;
    }
}
