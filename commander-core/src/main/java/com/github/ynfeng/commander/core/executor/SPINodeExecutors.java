package com.github.ynfeng.commander.core.executor;

import com.github.ynfeng.commander.core.definition.NodeDefinition;
import java.util.ServiceLoader;
import java.util.stream.StreamSupport;

public class SPINodeExecutors implements NodeExecutors {
    private ServiceLoader<NodeExecutor> nodeExecutors;

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
