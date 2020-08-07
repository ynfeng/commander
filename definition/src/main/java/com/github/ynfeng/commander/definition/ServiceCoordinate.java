package com.github.ynfeng.commander.core.definition;

public class ServiceCoordinate {
    private final String name;
    private final int version;

    private ServiceCoordinate(String name, int version) {
        this.name = name;
        this.version = version;
    }

    public static ServiceCoordinate of(String name, int version) {
        return new ServiceCoordinate(name, version);
    }

    public String name() {
        return name;
    }

    public int version() {
        return version;
    }
}
