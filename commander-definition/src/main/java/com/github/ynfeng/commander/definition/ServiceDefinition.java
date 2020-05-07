package com.github.ynfeng.commander.definition;

import com.github.ynfeng.commander.core.definition.NextableNodeDefinition;

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
