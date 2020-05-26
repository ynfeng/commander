package com.github.ynfeng.commander.core.definition;

@FunctionalInterface
public interface NodeDefinition {
    NodeDefinition NULL = new NodeDefinition() {
        @Override
        public String refName() {
            return null;
        }
    };

    String refName();
}
