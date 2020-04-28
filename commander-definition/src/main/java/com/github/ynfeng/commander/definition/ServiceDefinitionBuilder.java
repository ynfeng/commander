package com.github.ynfeng.commander.definition;

public class ServiceDefinitionBuilder extends NormalDefinitionBuilder {
    private final ServiceDefinition serviceDefinition;

    protected ServiceDefinitionBuilder(NodeDefinition pre,
                                       String refName,
                                       ServiceCoordinate serviceCoordinate) {
        serviceDefinition = new ServiceDefinition(refName, serviceCoordinate);
        ProcessDefinitionBuilderContext.currentDefinition(serviceDefinition);
        pre.next(serviceDefinition);
    }
}
