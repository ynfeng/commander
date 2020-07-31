package com.github.ynfeng.commander.server;

public class Server {

    private String name;

    public Server(ServerConfig config) {
        name = config.getName();
    }

    public static ServerBuilder builder() {
        return new ServerBuilder();
    }

    public String name() {
        return name;
    }
}
