package com.github.ynfeng.commander.cluster;

public interface Environment {
    <T> T getProperty(String name, T defaultValue);

    <T> T getProperty(String name);
}
