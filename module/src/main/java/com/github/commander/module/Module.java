package com.github.commander.module;

import com.github.ynfeng.commander.support.Named;

public interface Module extends Named {
    void init();

    <T> T getComponent(Class<T> componentType);

    <T> void registerComponent(Class<T> componentType, T component);
}
