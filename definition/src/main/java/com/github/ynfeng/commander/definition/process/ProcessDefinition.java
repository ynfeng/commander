package com.github.ynfeng.commander.definition.process;

import com.github.ynfeng.commander.definition.node.StartDefinition;
import lombok.AccessLevel;
import lombok.Setter;

@Setter(AccessLevel.PROTECTED)
public class ProcessDefinition {
    private final String name;
    private final int version;
    private StartDefinition start;

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

    public StartDefinition start() {
        return start;
    }
}
