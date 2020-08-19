package com.github.ynfeng.commander.engine.executor;

import com.github.ynfeng.commander.definition.NodeDefinition;

public interface NodeExecutors {
    NodeExecutor getExecutor(NodeDefinition node);
}
