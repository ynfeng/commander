package com.github.ynfeng.commander.definition.node;

public interface NodeDefinition {
    NodeDefinition EMPTY = new NodeDefinition() {
        @Override
        public NodeDefinition next() {
            return null;
        }
    };

    NodeDefinition next();
}
