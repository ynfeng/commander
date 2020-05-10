package com.github.ynfeng.commander.core.executor;

import com.github.ynfeng.commander.core.context.ProcessContext;
import com.github.ynfeng.commander.core.definition.NodeDefinition;

public interface NodeExecutor {
    void execute(ProcessContext context);

    boolean canExecute(NodeDefinition nodeDefinition);
}
