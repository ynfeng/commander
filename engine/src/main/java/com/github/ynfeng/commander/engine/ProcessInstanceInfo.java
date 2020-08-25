package com.github.ynfeng.commander.engine;

import com.github.ynfeng.commander.definition.NodeDefinition;
import java.util.List;

public class ProcessInstanceInfo {
    private final List<NodeDefinition> executedNodes;

    public ProcessInstanceInfo(List<NodeDefinition> executedNodes) {
        this.executedNodes = executedNodes;
    }

    public List<NodeDefinition> executedNodes() {
        return executedNodes;
    }
}
