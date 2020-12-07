package com.github.commander.module;

import com.google.common.collect.Maps;
import java.util.Map;

public abstract class AbstractModule<C extends ModuleConfig> implements Module<C> {
    private final Map<Class<? extends Component>, Component> componentRegistry = Maps.newHashMap();

    @Override
    public <T extends Component> Component getComponent(Class<T> componentType) {
        if (componentRegistry.get(componentType) == null) {
            throw new ModuleException(String.format("component '%s' not found in module '%s'",
                componentType.getName(), name()));
        }
        return componentRegistry.get(componentType);
    }

    protected <T extends Component> void registerComponent(Class<T> componentType, T component) {
        componentRegistry.put(componentType, component);
    }
}
