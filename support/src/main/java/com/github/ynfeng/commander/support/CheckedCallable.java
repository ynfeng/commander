package com.github.ynfeng.commander.support;

@FunctionalInterface
public interface CheckedCallable<T> {
    T call() throws Exception;
}
