package com.github.ynfeng.commander.engine.command;

import com.github.ynfeng.commander.definition.NodeDefinition;
import com.github.ynfeng.commander.engine.Variables;

public class ContineExecute implements EngineCommand {
    private NodeDefinition nodeDefinition;
    private final Variables variables;

    public ContineExecute(NodeDefinition nodeDefinition, Variables variables) {
        this.nodeDefinition = nodeDefinition;
        this.variables = variables;
    }

    public NodeDefinition nodeDefinition() {
        return nodeDefinition;
    }
}
