package com.github.ynfeng.commander.definition;

public interface NodeDefinition {
    NodeDefinition NULL = new NodeDefinition() {
        @Override
        public String refName() {
            throw new UnsupportedOperationException();
        }
    };

    String refName();
}
