package com.github.ynfeng.commander.definition;

public class ServiceDefinition extends NextableNodeDefinition {
    private final ServiceCoordinate serviceCoordinate;

    public ServiceDefinition(String refName, ServiceCoordinate serviceCoordinate) {
        super(refName);
        this.serviceCoordinate = serviceCoordinate;
    }

    public ServiceCoordinate serviceCoordinate() {
        return serviceCoordinate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ServiceDefinition)) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }

        ServiceDefinition that = (ServiceDefinition) o;

        return serviceCoordinate.equals(that.serviceCoordinate);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + serviceCoordinate.hashCode();
        return result;
    }
}
