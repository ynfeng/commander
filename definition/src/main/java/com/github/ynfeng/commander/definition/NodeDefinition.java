package com.github.ynfeng.commander.definition;

@FunctionalInterface
public interface NodeDefinition {
    NodeDefinition NULL = new NodeDefinition() {
        @Override
        public String refName() {
            return "NULL_NODE";
        }
    };

    String refName();
}
