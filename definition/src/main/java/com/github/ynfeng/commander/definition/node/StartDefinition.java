package com.github.ynfeng.commander.definition.node;

public class StartDefinition implements NodeDefinition, ParentNode {
    private NodeDefinition next;

    @Override
    public NodeDefinition next() {
        return next;
    }

    @Override
    public void setNext(NodeDefinition nodeDefinition) {
        next = nodeDefinition;
    }
}
