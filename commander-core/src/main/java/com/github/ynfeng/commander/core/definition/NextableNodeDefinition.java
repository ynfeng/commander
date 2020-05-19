package com.github.ynfeng.commander.core.definition;

public abstract class NextableNodeDefinition extends AbstractNodeDefinition implements Nextable {
    private NodeDefinition next = NodeDefinition.NULL;

    protected NextableNodeDefinition(String refName) {
        super(refName);
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
