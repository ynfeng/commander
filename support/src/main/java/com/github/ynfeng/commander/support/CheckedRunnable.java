package com.github.ynfeng.commander.support;

@FunctionalInterface
public interface CheckedRunnable {
    void run() throws Exception;
}
