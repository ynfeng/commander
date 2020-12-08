package com.github.commander.module;

import com.google.common.collect.Maps;
import java.util.Map;

public abstract class AbstractModule implements Module {
    private final Map<Class<?>, Object> componentRegistry = Maps.newHashMap();

    @Override
    public <T> T getComponent(Class<T> componentType) {
        if (componentRegistry.get(componentType) == null) {
            throw new ModuleException(String.format("component '%s' not found in module '%s'",
                componentType.getName(), name()));
        }
        return (T) componentRegistry.get(componentType);
    }

    @Override
    public <T> void registerComponent(Class<T> componentType, T component) {
        componentRegistry.put(componentType, component);
    }
}
