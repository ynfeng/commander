package com.github.ynfeng.commander.core.engine.executor;

import com.github.ynfeng.commander.core.definition.NodeDefinition;
import java.util.ServiceLoader;
import java.util.stream.StreamSupport;

public class SPINodeExecutors implements NodeExecutors {
    private ServiceLoader<NodeExecutor> nodeExecutors;
    private volatile boolean loaded;

    @Override
    public NodeExecutor getExecutor(NodeDefinition node) {
        tryLoad();
        return StreamSupport.stream(nodeExecutors.spliterator(), false)
            .filter(nodeExecutor -> nodeExecutor.canExecute(node))
            .findFirst()
            .orElse(null);
    }

    private void tryLoad() {
        if (!loaded) {
            synchronized (this) {
                if (!loaded) {
                    nodeExecutors = ServiceLoader.load(NodeExecutor.class);
                    loaded = true;
                }
            }
        }
    }
}
