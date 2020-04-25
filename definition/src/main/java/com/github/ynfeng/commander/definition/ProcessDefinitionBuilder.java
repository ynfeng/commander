package com.github.ynfeng.commander.definition;


public class ProcessDefinitionBuilder {
    private ProcessDefinitionBuilder() {
    }

    public static StartDefinitionBuilder create(String name, int version) {
        ProcessDefinition processDefinition = new ProcessDefinition(name, version);
        return new StartDefinitionBuilder(processDefinition);
    }
}
