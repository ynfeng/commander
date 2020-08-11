package com.github.ynfeng.commander.bootstrap;

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
