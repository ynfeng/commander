package com.github.ynfeng.commander.engine.executor;

import com.github.ynfeng.commander.engine.context.ProcessContext;
import com.github.ynfeng.commander.definition.ForkBranch;
import com.github.ynfeng.commander.definition.ForkDefinition;
import com.github.ynfeng.commander.definition.NodeDefinition;
import java.util.Iterator;

public class ForkNodeExecutor implements NodeExecutor {

    @Override
    public void execute(ProcessContext context, NodeDefinition nodeDefinition) {
        ForkDefinition forkDefinition = (ForkDefinition) nodeDefinition;
        Iterator<ForkBranch> branchIterator = forkDefinition.branchs().iterator();
        while (branchIterator.hasNext()) {
            ForkBranch branch = branchIterator.next();
            context.addReadyNode(branch.next());
        }
        context.completeNode(nodeDefinition);
    }

    @Override
    public boolean canExecute(NodeDefinition nodeDefinition) {
        return nodeDefinition instanceof ForkDefinition;
    }
}
