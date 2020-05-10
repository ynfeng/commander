package com.github.ynfeng.commander.core.executor;

import com.github.ynfeng.commander.core.context.ProcessContext;
import com.github.ynfeng.commander.core.definition.NodeDefinition;
import java.util.Objects;

public class DefaultNodeExecutor implements NodeExecutor {
    @Override
    public void execute(ProcessContext context) {

    }

    @Override
    public boolean canExecute(NodeDefinition nodeDefinition) {
        return Objects.isNull(nodeDefinition);
    }
}
