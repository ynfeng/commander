package com.github.ynfeng.commander.core.definition;


public class ProcessDefinition {
    private final String name;
    private final int version;
    private NodeDefinition firstNode;

    public ProcessDefinition(String name, int version) {
        this.name = name;
        this.version = version;
        firstNode = NodeDefinition.NULL;
    }

    public String name() {
        return name;
    }

    public int version() {
        return version;
    }

    public NodeDefinition firstNode() {
        return firstNode;
    }

    public void firstNode(NextableNodeDefinition startDefinition) {
        firstNode = startDefinition;
    }
}
