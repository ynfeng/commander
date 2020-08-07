package com.github.ynfeng.commander.server;

@FunctionalInterface
public interface StartFunction {
    AutoCloseable execute();
}
