package com.github.ynfeng.commander.engine;

import com.github.ynfeng.commander.definition.NodeDefinition;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;

public class ExecutedNodes {
    private final Queue<NodeDefinition> executedList = new LinkedBlockingQueue<NodeDefinition>();

    public void add(NodeDefinition nodeDefinition) {
        executedList.add(nodeDefinition);
    }

    public List<NodeDefinition> toList() {
        return executedList.stream()
            .distinct()
            .collect(Collectors.toList());
    }
}
