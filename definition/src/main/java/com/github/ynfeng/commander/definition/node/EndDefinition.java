package com.github.ynfeng.commander.definition.node;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EndDefinition implements NodeDefinition {
    public static EndDefinition create() {
        return new EndDefinition();
    }

    @Override
    public NodeDefinition next() {
        return NodeDefinition.EMPTY;
    }
}
