package com.github.ynfeng.commander.engine;

import com.github.ynfeng.commander.definition.NodeDefinition;
import com.github.ynfeng.commander.engine.executor.NodeExecutingVariable;
import java.util.concurrent.CompletableFuture;

public interface ProcessInstance {
    void start();

    void addReadyNode(NodeDefinition nodeDefinition);

    void nodeComplete(NodeDefinition nodeDefinition);

    void processComplete();

    CompletableFuture<NodeExecutingVariable> getNodeExecutingVariable(String refName);

    CompletableFuture<NodeExecutingVariable> setNodeExecutingVariable(String refName, String key, Object val);
}
