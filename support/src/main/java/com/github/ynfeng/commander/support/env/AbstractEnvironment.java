package com.github.ynfeng.commander.support.env;

import java.util.Optional;

public abstract class AbstractEnvironment implements Environment {
    private final PropertySource propertySource;

    protected AbstractEnvironment(PropertySource propertySource) {
        this.propertySource = propertySource;
    }

    @Override
    public <T> T getProperty(String name, T defaultValue) {
        try {
            Optional<T> resultCandicate = Optional.ofNullable(propertySource.getProperty(name));
            return resultCandicate.orElse(defaultValue);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    @Override
    public <T> T getProperty(String name) {
        try {
            return propertySource.getProperty(name);
        } catch (Exception e) {
            return null;
        }
    }
}
