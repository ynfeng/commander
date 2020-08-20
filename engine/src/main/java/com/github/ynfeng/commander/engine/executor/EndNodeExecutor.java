package com.github.ynfeng.commander.engine.executor;

import com.github.ynfeng.commander.definition.EndDefinition;
import com.github.ynfeng.commander.definition.NodeDefinition;
import com.github.ynfeng.commander.engine.ProcessInstance;

public class EndNodeExecutor implements NodeExecutor {

    @Override
    public void execute(ProcessInstance processInstance, NodeDefinition nodeDefinition) {
        processInstance.addReadyNode(NodeDefinition.NULL);
        processInstance.nodeComplete(nodeDefinition);
        processInstance.processComplete();
    }

    @Override
    public boolean canExecute(NodeDefinition nodeDefinition) {
        return nodeDefinition instanceof EndDefinition;
    }
}
