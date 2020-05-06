package com.github.ynfeng.commander.definition;

public interface Nextable {
    <T extends NodeDefinition> T next();

    void next(NodeDefinition nodeDefinition);
}
