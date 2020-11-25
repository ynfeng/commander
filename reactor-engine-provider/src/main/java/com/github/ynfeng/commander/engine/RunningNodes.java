package com.github.ynfeng.commander.engine;

import com.github.ynfeng.commander.definition.NodeDefinition;
import com.google.common.collect.Maps;
import java.util.Map;
import java.util.Optional;

public class RunningNodes {
    private final Map<String, NodeDefinition> runningNodes = Maps.newHashMap();

    public void add(NodeDefinition nodeDefinition) {
        runningNodes.put(nodeDefinition.refName(), nodeDefinition);
    }

    public void remove(String refName) {
        runningNodes.remove(refName);
    }

    public Optional<NodeDefinition> get(String refName) {
        return Optional.ofNullable(runningNodes.get(refName));
    }
}
