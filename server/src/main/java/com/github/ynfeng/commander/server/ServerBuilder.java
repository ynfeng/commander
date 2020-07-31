package com.github.ynfeng.commander.server;

public class ServerBuilder {
    private final ServerConfig config = new ServerConfig();

    public Server build() {
        return new Server(config);
    }

    public ServerBuilder withName(String name) {
        config.setName(name);
        return this;
    }
}
