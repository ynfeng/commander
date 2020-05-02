package com.github.ynfeng.commander.definition;

public class ServiceDefinition extends AbstractNodeDefinition {
    private final ServiceCoordinate serviceCoordinate;
    private NodeDefinition next;

    protected ServiceDefinition(String refName, ServiceCoordinate serviceCoordinate) {
        super(refName);
        this.serviceCoordinate = serviceCoordinate;
    }

    @Override
    public void next(NodeDefinition nodeDefinition) {
        next = nodeDefinition;
    }

    public ServiceCoordinate serviceCoordinate() {
        return serviceCoordinate;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends NodeDefinition> T next() {
        return (T) next;
    }
}
