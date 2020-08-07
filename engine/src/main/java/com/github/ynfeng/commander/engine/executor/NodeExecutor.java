package com.github.ynfeng.commander.engine.executor;

import com.github.ynfeng.commander.engine.context.ProcessContext;
import com.github.ynfeng.commander.definition.NodeDefinition;

public interface NodeExecutor {
    void execute(ProcessContext processContext, NodeDefinition nodeDefinition);

    boolean canExecute(NodeDefinition nodeDefinition);
}
