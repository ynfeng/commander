package com.github.ynfeng.commander.core.definition;


public class ProcessDefinition {
    private final String name;
    private final int version;
    private NextableNodeDefinition start;

    public ProcessDefinition(String name, int version) {
        this.name = name;
        this.version = version;
    }

    public String name() {
        return name;
    }

    public int version() {
        return version;
    }

    public NextableNodeDefinition start() {
        return start;
    }

    public void start(NextableNodeDefinition startDefinition) {
        start = startDefinition;
    }
}
