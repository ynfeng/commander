package com.github.ynfeng.commander.engine;

public interface ProcessEngine {
    void startup();

    ProcessFuture startProcess(String name, int version, Variables variables);

    ProcessFuture startProcess(String name, int version);

    void shutdown();
}
