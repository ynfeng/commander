package com.github.ynfeng.commander.core.executor;

import com.github.ynfeng.commander.core.definition.NodeDefinition;
import com.github.ynfeng.commander.core.engine.ProcessEngineException;
import java.util.ServiceLoader;
import java.util.stream.StreamSupport;

public class NodeExecutors {
    private ServiceLoader<NodeExecutor> nodeExecutors;

    public void load() {
        nodeExecutors = ServiceLoader.load(NodeExecutor.class);
    }

    public NodeExecutor getExecutor(NodeDefinition node) {
        return StreamSupport.stream(nodeExecutors.spliterator(), false)
            .filter(nodeExecutor -> nodeExecutor.canExecute(node))
            .findFirst()
            .orElseThrow(() -> new ProcessEngineException("Can't find any executor for " + node.refName()));
    }
}
