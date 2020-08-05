package com.github.ynfeng.commander.server;

public class ShutdownStep {
    private final String name;
    private final AutoCloseable function;

    public ShutdownStep(String name, AutoCloseable function) {
        this.name = name;
        this.function = function;
    }

    public void execute() throws Exception {
        function.close();
    }
}
