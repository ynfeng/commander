package com.github.ynfeng.commander.server;

public class ServerBuilder {
    private final ServerConfig config = new ServerConfig();

    public ServerBuilder withName(String name) {
        config.setName(name);
        return this;
    }

    public ServerBuilder withAddress(Address address) {
        config.setAddress(address);
        return this;
    }

    public ServerBuilder withStartStep(String name, StartFunction function) {
        config.addStartStep(new StartStep(name, function));
        return this;
    }

    public Server build() {
        return new Server(config);
    }

    public ServerBuilder withRole(Role role) {
        config.setRole(role);
        return this;
    }
}
