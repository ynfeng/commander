package com.github.ynfeng.commander.engine;

import com.github.ynfeng.commander.definition.NodeDefinition;

public class AddReadyNode implements EngineCommand {
    private final NodeDefinition nextNode;

    public AddReadyNode(NodeDefinition nextNode) {
        this.nextNode = nextNode;
    }

    public NodeDefinition nextNode() {
        return nextNode;
    }
}
