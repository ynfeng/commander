package com.github.ynfeng.commander.server;

@FunctionalInterface
public interface CheckedCallable<T> {
    T call() throws Exception;
}
