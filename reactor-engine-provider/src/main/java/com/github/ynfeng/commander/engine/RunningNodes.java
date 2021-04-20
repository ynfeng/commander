package com.github.ynfeng.commander.engine;

import com.github.ynfeng.commander.definition.NodeDefinition;
import com.google.common.collect.Maps;
import java.util.Map;
import java.util.Optional;

public class RunningNodes {
    private final Map<String, NodeDefinition> running = Maps.newConcurrentMap();

    public void add(NodeDefinition nodeDefinition) {
        if (nodeDefinition != null && nodeDefinition != NodeDefinition.NULL) {
            running.put(nodeDefinition.refName(), nodeDefinition);
        }
    }

    public void remove(String refName) {
        running.remove(refName);
    }

    public Optional<NodeDefinition> get(String refName) {
        return Optional.ofNullable(running.get(refName));
    }

    public int size() {
        return running.size();
    }
}
