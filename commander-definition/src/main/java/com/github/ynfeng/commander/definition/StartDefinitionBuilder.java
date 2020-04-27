package com.github.ynfeng.commander.definition;

public class StartDefinitionBuilder implements EndableBuilder {
    private StartDefinition start;

    protected StartDefinitionBuilder() {
    }

    public StartDefinitionBuilder start() {
        start = new StartDefinition();
        ProcessDefinitionBuilderContext.processDefinition().start(start);
        return this;
    }

    @Override
    public EndDefinitionBuilder end() {
        return new EndDefinitionBuilder(start);
    }

    public EndableBuilder service(String refName, ServiceCoordinate serviceCoordinate) {
        return new ServiceDefinitionBuilder(start, refName, serviceCoordinate);
    }
}
