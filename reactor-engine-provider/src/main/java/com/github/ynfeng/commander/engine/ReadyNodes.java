package com.github.ynfeng.commander.engine;

import com.github.ynfeng.commander.definition.NodeDefinition;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public class ReadyNodes {
    private final Queue<NodeDefinition> readyNodes = new LinkedBlockingQueue<>();

    public NodeDefinition poll() {
        return readyNodes.poll();
    }

    public void add(NodeDefinition nodeDefinition) {
        if (nodeDefinition != null && nodeDefinition != NodeDefinition.NULL) {
            readyNodes.add(nodeDefinition);
        }
    }

    public int size() {
        return readyNodes.size();
    }
}
