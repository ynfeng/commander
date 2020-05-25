package com.github.ynfeng.commander.core.executor;

import com.github.ynfeng.commander.core.context.ProcessContext;
import com.github.ynfeng.commander.core.definition.JoinDefinition;
import com.github.ynfeng.commander.core.definition.NodeDefinition;
import java.util.List;

public class JoinNodeExecutor implements NodeExecutor {
    @Override
    public void execute(NodeDefinition nodeDefinition) {
        ProcessContext context = ProcessContext.get();
        JoinDefinition joinDefinition = (JoinDefinition) nodeDefinition;
        List<String> ons = joinDefinition.ons();
        List<String> executedNodes = context.executedNodes();
        if (isNotExecuted(nodeDefinition.refName(), executedNodes) && executedNodes.containsAll(ons)) {
            context.addReadyNode(joinDefinition.next());
            context.completeNode(nodeDefinition);
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
