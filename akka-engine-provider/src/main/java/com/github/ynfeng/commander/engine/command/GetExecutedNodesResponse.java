package com.github.ynfeng.commander.engine.command;

import com.github.ynfeng.commander.definition.NodeDefinition;
import java.util.List;
import java.util.stream.Collectors;

public class GetExecutedNodesResponse implements EngineCommand {

    private final List<NodeDefinition> executedNodes;

    public GetExecutedNodesResponse(List<NodeDefinition> executedNodes) {
        this.executedNodes = executedNodes;
    }

    public List<String> executedNodes() {
        return executedNodes.stream().map(NodeDefinition::refName).collect(Collectors.toList());
    }
}
