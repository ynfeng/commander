package com.github.ynfeng.commander.server;

public class StartStep {
    private final String name;
    private final StartFunction function;

    public StartStep(String name, StartFunction function) {
        this.name = name;
        this.function = function;
    }

    public AutoCloseable execute() {
        return function.execute();
    }

    public String name() {
        return name;
    }
}
