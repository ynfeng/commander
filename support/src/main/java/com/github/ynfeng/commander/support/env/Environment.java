package com.github.ynfeng.commander.support.env;

public interface Environment {
    <T> T getProperty(String name, T defaultValue);

    <T> T getProperty(String name);

    String name();
}
