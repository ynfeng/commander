package com.github.ynfeng.commander.definition;

public class ServiceDefinition extends NextableNodeDefinition {
    private final ServiceCoordinate serviceCoordinate;

    protected ServiceDefinition(String refName, ServiceCoordinate serviceCoordinate) {
        super(refName);
        this.serviceCoordinate = serviceCoordinate;
    }

    public ServiceCoordinate serviceCoordinate() {
        return serviceCoordinate;
    }

}
