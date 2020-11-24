package com.github.ynfeng.commander.engine;

import com.github.ynfeng.commander.definition.NodeDefinition;
import com.google.common.collect.Lists;
import java.util.List;

public class ExecutedNodes {
    private final List<NodeDefinition> executedList = Lists.newArrayList();

    public void add(NodeDefinition nodeDefinition) {
        executedList.add(nodeDefinition);
    }

    public List<NodeDefinition> toList() {
        return Lists.newArrayList(executedList);
    }
}
