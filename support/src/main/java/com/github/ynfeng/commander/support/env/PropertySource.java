package com.github.ynfeng.commander.support.env;

public interface PropertySource {
    <T> T getProperty(String name);
}
