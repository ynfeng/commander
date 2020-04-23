package com.github.ynfeng.commander.definition.process;

import com.github.ynfeng.commander.definition.node.StartDefinition;

public class ProcessDefinitionBuilder {
    private final String name;
    private final int version;
    private StartDefinition start;

    private ProcessDefinitionBuilder(String name, int version) {
        this.name = name;
        this.version = version;
    }

    public static ProcessDefinitionBuilder create(String name, int version) {
        return new ProcessDefinitionBuilder(name, version);
    }

    public ProcessDefinition build() {
        ProcessDefinition processDefinition = new ProcessDefinition(name, version);
        processDefinition.setStart(start);
        return processDefinition;
    }

    public ProcessDefinitionBuilder start() {
        start = new StartDefinition();
        return this;
    }
}
