package com.github.ynfeng.commander.engine;

import com.github.ynfeng.commander.definition.NodeDefinition;
import com.google.common.collect.Lists;
import java.util.Optional;
import java.util.Queue;

public class ReadyNodes {
    private final Queue<NodeDefinition> readyNodes = Lists.newLinkedList();

    public NodeDefinition poll() {
        return readyNodes.poll();
    }

    public void add(NodeDefinition nodeDefinition) {
        readyNodes.add(nodeDefinition);
    }

    public NodeDefinition peek() {
        return readyNodes.peek();
    }

    public Optional<NodeDefinition> get(String nodeRefName) {
        return readyNodes.stream()
            .filter(each -> each.refName().equals(nodeRefName))
            .findAny();
    }
}
