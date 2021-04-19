package com.github.ynfeng.commander.definition;

@FunctionalInterface
public interface NodeDefinition {
    NodeDefinition NULL = () -> "NULL_NODE";

    String refName();
}
