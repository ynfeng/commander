package com.github.ynfeng.commander.cluster;

public interface PropertySource {
    <T> T getProperty(String name);
}
