package com.github.ynfeng.commander.engine.command;

import com.github.ynfeng.commander.definition.NodeDefinition;

public class ExecuteNode implements EngineCommand {
    private final NodeDefinition nodeDefinition;

    public ExecuteNode(NodeDefinition nodeDefinition) {
        this.nodeDefinition = nodeDefinition;
    }

    public NodeDefinition nodeDefinition() {
        return nodeDefinition;
    }
}
