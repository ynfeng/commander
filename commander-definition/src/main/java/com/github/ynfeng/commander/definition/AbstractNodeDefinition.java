package com.github.ynfeng.commander.definition;

public abstract class AbstractNodeDefinition implements NodeDefinition {
    private final String refName;
    private NodeDefinition next;

    protected AbstractNodeDefinition(String refName) {
        this.refName = refName;
    }

    @Override
    public String refName() {
        return refName;
    }

    @Override
    public void next(NodeDefinition nodeDefinition) {
        next = nodeDefinition;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends NodeDefinition> T next() {
        return (T) next;
    }
}
