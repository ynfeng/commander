package com.github.ynfeng.commander.server;

@FunctionalInterface
public interface CheckedRunnable {
    void run() throws Exception;
}
