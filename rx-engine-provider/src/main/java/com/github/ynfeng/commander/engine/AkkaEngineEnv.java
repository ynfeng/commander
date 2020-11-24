package com.github.ynfeng.commander.engine;

import com.github.ynfeng.commander.engine.executor.NodeExecutors;
import com.github.ynfeng.commander.engine.executor.SPINodeExecutors;
import com.github.ynfeng.commander.support.env.PropertySource;

public class AkkaEngineEnv extends EngineEnvironment {
    protected AkkaEngineEnv(PropertySource propertySource) {
        super(propertySource);
    }

    @Override
    public ProcessIdGenerator getProcessIdGenerator() {
        return new UUIDProcessIdGenerator();
    }

    @Override
    public NodeExecutors getNodeExecutors() {
        return new SPINodeExecutors();
    }

    @Override
    public String name() {
        return "akka-engine";
    }
}
