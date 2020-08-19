package com.github.ynfeng.commander.engine.executor;

import com.github.ynfeng.commander.definition.NodeDefinition;
import com.github.ynfeng.commander.engine.context.ProcessContext;

public interface NodeExecutor {
    void execute(ProcessContext processContext, NodeDefinition nodeDefinition);

    boolean canExecute(NodeDefinition nodeDefinition);
}
