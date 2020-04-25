package com.github.ynfeng.commander.definition;

public interface NodeDefinition {
    NodeDefinition EMPTY = new NodeDefinition() {
        @Override
        public NodeDefinition next() {
            return null;
        }
    };

    NodeDefinition next();
}
