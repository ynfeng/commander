package com.github.ynfeng.commander.engine;

import com.github.ynfeng.commander.definition.NodeDefinition;
import java.util.List;

public class ProcessInstanceInfo {
    private final List<NodeDefinition> executeNodes;

    public ProcessInstanceInfo(List<NodeDefinition> executeNodes) {
        this.executeNodes = executeNodes;
    }

    public List<NodeDefinition> executeNodes() {
        return executeNodes;
    }
}
