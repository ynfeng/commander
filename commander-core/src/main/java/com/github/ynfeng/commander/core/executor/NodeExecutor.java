package com.github.ynfeng.commander.core.executor;

import com.github.ynfeng.commander.core.definition.NodeDefinition;

public interface NodeExecutor {
    void execute(NodeDefinition nodeDefinition);

    boolean canExecute(NodeDefinition nodeDefinition);
}
