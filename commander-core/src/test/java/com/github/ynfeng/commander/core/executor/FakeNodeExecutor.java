package com.github.ynfeng.commander.core.executor;

import com.github.ynfeng.commander.core.context.ProcessContext;
import com.github.ynfeng.commander.core.definition.FakeNodeDefinition;
import com.github.ynfeng.commander.core.definition.NodeDefinition;

public class FakeNodeExecutor implements NodeExecutor {

    @Override
    public void execute(ProcessContext context) {
        context.completeCurrentNode(NodeDefinition.NULL);
    }

    @Override
    public boolean canExecute(NodeDefinition nodeDefinition) {
        return nodeDefinition instanceof FakeNodeDefinition;
    }
}
