package com.github.ynfeng.commander.engine;

import com.github.ynfeng.commander.definition.NodeDefinition;

public interface ProcessInstance {
    void start();

    void addReadyNode(NodeDefinition nodeDefinition);

    void nodeComplete(NodeDefinition nodeDefinition);

    void processComplete();
}
