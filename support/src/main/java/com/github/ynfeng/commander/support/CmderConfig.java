package com.github.ynfeng.commander.support;

public interface CmderConfig {
    <T> T getConfig(String name, T defaultValue);
}
