package com.github.ynfeng.commander.core.executor;

import com.github.ynfeng.commander.core.context.ProcessContext;
import com.github.ynfeng.commander.core.definition.ForkBranch;
import com.github.ynfeng.commander.core.definition.ForkDefinition;
import com.github.ynfeng.commander.core.definition.NodeDefinition;
import java.util.Iterator;

public class ForkNodeExecutor implements NodeExecutor {

    @Override
    public void execute(NodeDefinition nodeDefinition) {
        ProcessContext context = ProcessContext.get();
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
