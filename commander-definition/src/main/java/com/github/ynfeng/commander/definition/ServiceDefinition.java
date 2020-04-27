package com.github.ynfeng.commander.definition;

public class ServiceDefinition implements ParentDefintion {
    private final String refName;
    private final ServiceCoordinate serviceCoordinate;
    private NodeDefinition next;

    protected ServiceDefinition(String refName, ServiceCoordinate serviceCoordinate) {
        this.refName = refName;
        this.serviceCoordinate = serviceCoordinate;
    }

    @Override
    public void next(NodeDefinition nodeDefinition) {
        next = nodeDefinition;
    }

    public String refName() {
        return refName;
    }

    public ServiceCoordinate serviceCoordinate() {
        return serviceCoordinate;
    }

    @Override
    public <T extends NodeDefinition> T next() {
        return (T) next;
    }
}
