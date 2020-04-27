package com.github.ynfeng.commander.definition;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EndDefinition implements NodeDefinition {
    public static EndDefinition create() {
        return new EndDefinition();
    }

    @Override
    public <T extends NodeDefinition> T next() {
        return (T) NodeDefinition.EMPTY;
    }
}
