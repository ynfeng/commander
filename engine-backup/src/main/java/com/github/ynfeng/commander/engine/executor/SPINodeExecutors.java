package com.github.ynfeng.commander.engine.executor;

import com.github.ynfeng.commander.definition.NodeDefinition;
import java.util.ServiceLoader;
import java.util.stream.StreamSupport;

public class SPINodeExecutors implements NodeExecutors {
    private final ServiceLoader<NodeExecutor> nodeExecutors;

    public SPINodeExecutors() {
        nodeExecutors = ServiceLoader.load(NodeExecutor.class);
    }

    @Override
    public NodeExecutor getExecutor(NodeDefinition node) {
        return StreamSupport.stream(nodeExecutors.spliterator(), false)
            .filter(nodeExecutor -> nodeExecutor.canExecute(node))
            .findFirst()
            .orElse(null);
    }
}
