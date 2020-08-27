package com.github.ynfeng.commander.engine;

import com.github.ynfeng.commander.engine.executor.NodeExecutors;
import com.github.ynfeng.commander.support.env.AbstractEnvironment;
import com.github.ynfeng.commander.support.env.PropertySource;

public abstract class EngineEnvironment extends AbstractEnvironment {
    protected EngineEnvironment(PropertySource propertySource) {
        super(propertySource);
    }

    public abstract ProcessIdGenerator getProcessIdGenerator();

    public abstract NodeExecutors getNodeExecutors();
}
