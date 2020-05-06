package com.github.ynfeng.commander.core.definition;

public interface Nextable {
    <T extends NodeDefinition> T next();

    void next(NodeDefinition nodeDefinition);
}
