package com.github.ynfeng.commander.definition;

public interface NodeDefinition {
    NodeDefinition EMPTY = new NodeDefinition() {
        @SuppressWarnings("unchecked")
        @Override
        public NodeDefinition next() {
            return null;
        }
    };

    <T extends NodeDefinition> T next();
}
