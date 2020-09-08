package com.github.ynfeng.commander.engine.executor;

import com.github.ynfeng.commander.definition.ForkBranch;
import com.github.ynfeng.commander.definition.ForkDefinition;
import com.github.ynfeng.commander.definition.NodeDefinition;
import com.github.ynfeng.commander.engine.ProcessInstance;
import java.util.Iterator;

public class ForkNodeExecutor implements NodeExecutor {

    @Override
    public void execute(ProcessInstance processInstance, NodeDefinition nodeDefinition) {
        ForkDefinition forkDefinition = (ForkDefinition) nodeDefinition;
        Iterator<ForkBranch> branchIterator = forkDefinition.branchs().iterator();
        while (branchIterator.hasNext()) {
            ForkBranch branch = branchIterator.next();
            processInstance.addReadyNode(branch.next());
        }
        processInstance.nodeComplete(nodeDefinition);
    }

    @Override
    public boolean canExecute(NodeDefinition nodeDefinition) {
        return nodeDefinition instanceof ForkDefinition;
    }
}
