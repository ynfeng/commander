package com.github.ynfeng.commander.bootstrap;

@FunctionalInterface
public interface StartFunction {
    AutoCloseable execute();
}
