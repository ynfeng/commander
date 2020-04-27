package com.github.ynfeng.commander.definition;

public class ProcessDefinition {
    private final String name;
    private final int version;
    private StartDefinition start;

    protected ProcessDefinition(String name, int version) {
        this.name = name;
        this.version = version;
    }

    public String name() {
        return name;
    }

    public int version() {
        return version;
    }

    public StartDefinition start() {
        return start;
    }

    protected void start(StartDefinition startDefinition) {
        start = startDefinition;
    }
}
