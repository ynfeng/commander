package com.github.ynfeng.commander.definition;

public abstract class NormalDefinitionBuilder implements EndableBuilder {

    public NormalDefinitionBuilder service(String refName, ServiceCoordinate serviceCoordinate) {
        return new ServiceDefinitionBuilder(ProcessDefinitionBuilderContext.currentDefinition(),
            refName,
            serviceCoordinate);
    }

    @Override
    public EndDefinitionBuilder end() {
        return new EndDefinitionBuilder(ProcessDefinitionBuilderContext.currentDefinition());
    }
}
