package com.github.commander.module;

public interface ModuleManager {
    <T extends Module> T getModule(Class<T> moduleType);
}
