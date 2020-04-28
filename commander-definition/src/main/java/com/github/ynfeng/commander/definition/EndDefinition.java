package com.github.ynfeng.commander.definition;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EndDefinition implements NodeDefinition {
    protected static EndDefinition create() {
        return new EndDefinition();
    }

    @Override
    public <T extends NodeDefinition> T next() {
        return (T) NodeDefinition.NULL;
    }

    @Override
    public void next(NodeDefinition nodeDefinition) {
        throw new UnsupportedOperationException();
    }
}
