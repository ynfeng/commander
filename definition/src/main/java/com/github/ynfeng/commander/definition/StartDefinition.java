package com.github.ynfeng.commander.definition;

public class StartDefinition implements NodeDefinition, ParentDefintion {
    private NodeDefinition next;

    @Override
    public NodeDefinition next() {
        return next;
    }

    @Override
    public void next(NodeDefinition nodeDefinition) {
        next = nodeDefinition;
    }
}
