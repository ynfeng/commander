package com.github.ynfeng.commander.engine.command;

import com.github.ynfeng.commander.definition.NodeDefinition;

public class NodeComplete implements EngineCommand {
    private final NodeDefinition nodeDefinition;

    public NodeComplete(NodeDefinition nodeDefinition) {
        this.nodeDefinition = nodeDefinition;
    }

    public NodeDefinition nodeDefinition() {
        return nodeDefinition;
    }
}
