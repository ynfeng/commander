package com.github.ynfeng.commander.definition;

public class StartDefinitionBuilder extends NormalDefinitionBuilder {
    private StartDefinition start;

    protected StartDefinitionBuilder() {
    }

    public StartDefinitionBuilder start() {
        start = new StartDefinition();
        ProcessDefinitionBuilderContext.processDefinition().start(start);
        ProcessDefinitionBuilderContext.currentDefinition(start);
        return this;
    }
}
