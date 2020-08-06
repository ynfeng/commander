package com.github.ynfeng.commander.server;

public class Server {
    private final String name;
    private final Address address;
    private final StartSteps startSteps = new StartSteps();
    private ShutdownSteps shutdownSteps = new ShutdownSteps();

    protected Server(ServerConfig config) {
        name = config.getName();
        address = config.getAddress();
        startSteps.addAll(config.getStartSteps());
    }

    public static ServerBuilder builder() {
        return new ServerBuilder();
    }

    public String name() {
        return name;
    }

    public Address address() {
        return address;
    }

    public void startup() {
        shutdownSteps = startSteps.startupStepByStep();
    }

    public void shutdown() {
        shutdownSteps.shutdownStepByStep();
    }
}