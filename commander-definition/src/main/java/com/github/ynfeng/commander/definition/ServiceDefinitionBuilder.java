package com.github.ynfeng.commander.definition;

public class ServiceDefinitionBuilder implements EndableBuilder {
    private final ServiceDefinition serviceDefinition;

    protected ServiceDefinitionBuilder(ParentDefintion pre,
                                       String refName,
                                       ServiceCoordinate serviceCoordinate) {
        serviceDefinition = new ServiceDefinition(refName, serviceCoordinate);
        pre.next(serviceDefinition);
    }

    @Override
    public EndDefinitionBuilder end() {
        return new EndDefinitionBuilder(serviceDefinition);
    }
}
