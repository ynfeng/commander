package com.github.ynfeng.commander.definition;

public class EndDefinition extends AbstractNodeDefinition {

    public EndDefinition(String refName) {
        super(refName);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends NodeDefinition> T next() {
        return (T) NodeDefinition.NULL;
    }

    @Override
    public void next(NodeDefinition nodeDefinition) {
        throw new UnsupportedOperationException();
    }
}
