package com.github.ynfeng.commander.engine.executor;

import com.github.ynfeng.commander.definition.NodeDefinition;
import com.github.ynfeng.commander.engine.ProcessInstance;

public interface NodeExecutor {
    void execute(ProcessInstance processInstance, NodeDefinition nodeDefinition);

    boolean canExecute(NodeDefinition nodeDefinition);
}
