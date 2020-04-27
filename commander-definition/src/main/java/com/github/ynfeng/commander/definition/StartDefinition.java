package com.github.ynfeng.commander.definition;

public class StartDefinition implements ParentDefintion {
    private NodeDefinition next;

    @Override
    public void next(NodeDefinition nodeDefinition) {
        next = nodeDefinition;
    }

    @Override
    public <T extends NodeDefinition> T next() {
        return (T) next;
    }
}
