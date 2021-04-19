package com.github.ynfeng.commander.engine.executor;

import com.github.ynfeng.commander.definition.NodeDefinition;
import com.github.ynfeng.commander.definition.ServiceDefinition;
import com.github.ynfeng.commander.engine.ProcessInstance;
import com.github.ynfeng.commander.support.logger.CmderLogger;
import com.github.ynfeng.commander.support.logger.CmderLoggerFactory;
import java.util.concurrent.CompletableFuture;

public class ServiceNodeExecutor implements NodeExecutor {
    private final CmderLogger logger = CmderLoggerFactory.getSystemLogger();

    @Override
    public void execute(ProcessInstance processInstance, NodeDefinition nodeDefinition) {
        ServiceDefinition serviceDefinition = (ServiceDefinition) nodeDefinition;
        CompletableFuture<NodeExecutingVariable> variableFuture =
            processInstance.getNodeExecutingVariable(nodeDefinition.refName());
        variableFuture.thenAccept(variable -> {
            ServiceNodeExecuteState state = variable.get("state", ServiceNodeExecuteState.CREATED);
            logger.debug(String.format("%s's state is %s", nodeDefinition.refName(), state));
            state.accept(processInstance, serviceDefinition);
        });
    }

    @Override
    public boolean canExecute(NodeDefinition nodeDefinition) {
        return nodeDefinition instanceof ServiceDefinition;
    }
}
