package com.github.ynfeng.commander.engine.command;

import com.github.ynfeng.commander.definition.NodeDefinition;
import com.github.ynfeng.commander.engine.command.EngineCommand;

public class ProcessInstanceStart implements EngineCommand {
    private final NodeDefinition firstNode;

    public ProcessInstanceStart(NodeDefinition firstNode) {
        this.firstNode = firstNode;
    }

    public NodeDefinition firstNode() {
        return firstNode;
    }
}
