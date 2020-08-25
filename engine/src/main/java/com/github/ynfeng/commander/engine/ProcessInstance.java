package com.github.ynfeng.commander.engine;

import com.github.ynfeng.commander.definition.NodeDefinition;
import java.util.List;

public interface ProcessInstance {
    void start();

    void addReadyNode(NodeDefinition nodeDefinition);

    void nodeComplete(NodeDefinition nodeDefinition);

    List<NodeDefinition> getExecutedNodes();

    void processComplete();
}
