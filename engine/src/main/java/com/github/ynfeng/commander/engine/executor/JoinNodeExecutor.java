package com.github.ynfeng.commander.engine.executor;

import com.github.ynfeng.commander.definition.JoinDefinition;
import com.github.ynfeng.commander.definition.NodeDefinition;
import com.github.ynfeng.commander.engine.ProcessInstance;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class JoinNodeExecutor implements NodeExecutor {

    @Override
    public void execute(ProcessInstance processInstance, NodeDefinition nodeDefinition) {
        JoinDefinition joinDefinition = (JoinDefinition) nodeDefinition;
        CompletableFuture<List<String>> future = processInstance.executedNodes();
        future.thenAccept(executedNodes ->
            executeJoin(processInstance, joinDefinition, executedNodes));
    }

    private void executeJoin(ProcessInstance processInstance,
                             JoinDefinition joinDefinition,
                             List<String> executedNodes) {
        List<String> ons = joinDefinition.ons();
        if (isNotExecuted(joinDefinition.refName(), executedNodes) && executedNodes.containsAll(ons)) {
            processInstance.addReadyNode(joinDefinition.next());
            processInstance.nodeComplete(joinDefinition);
        }
    }

    private boolean isNotExecuted(String refName, List<String> executedNodes) {
        return !executedNodes.contains(refName);
    }

    @Override
    public boolean canExecute(NodeDefinition nodeDefinition) {
        return nodeDefinition instanceof JoinDefinition;
    }
}
