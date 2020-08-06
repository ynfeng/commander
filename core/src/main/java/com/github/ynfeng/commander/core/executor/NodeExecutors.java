package com.github.ynfeng.commander.core.executor;

import com.github.ynfeng.commander.core.definition.NodeDefinition;

public interface NodeExecutors {
    NodeExecutor getExecutor(NodeDefinition node);
}