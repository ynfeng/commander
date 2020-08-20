package com.github.ynfeng.commander.engine.executor;

import com.github.ynfeng.commander.definition.NodeDefinition;
import com.github.ynfeng.commander.definition.StartDefinition;
import com.github.ynfeng.commander.engine.ProcessInstance;

public class StartNodeExecutor implements NodeExecutor {

    @Override
    public void execute(ProcessInstance processInstance, NodeDefinition nodeDefinition) {
        StartDefinition startDefinition = (StartDefinition) nodeDefinition;
        processInstance.start();
        processInstance.addReadyNode(startDefinition.next());
        processInstance.nodeComplete(startDefinition);
    }

    @Override
    public boolean canExecute(NodeDefinition nodeDefinition) {
        return nodeDefinition instanceof StartDefinition;
    }
}
