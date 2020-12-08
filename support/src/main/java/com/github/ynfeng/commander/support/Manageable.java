package com.github.ynfeng.commander.support;

public interface Manageable {

    void start();

    void shutdown();

    boolean isStarted();
}
