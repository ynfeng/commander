package com.github.ynfeng.commander.definition;

public class StartDefinitionBuilder implements EndableBuilder {
    private StartDefinition start;
    private final ProcessDefinition processDefinition;

    protected StartDefinitionBuilder(ProcessDefinition processDefinition) {
        this.processDefinition = processDefinition;
    }

    public StartDefinitionBuilder start() {
        start = new StartDefinition();
        processDefinition.start(start);
        return this;
    }

    @Override
    public EndDefinitionBuilder end() {
        return new EndDefinitionBuilder(processDefinition, start);
    }
}
