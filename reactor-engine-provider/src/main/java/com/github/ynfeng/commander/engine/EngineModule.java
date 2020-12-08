package com.github.ynfeng.commander.engine;

import com.github.commander.module.AbstractModule;
import com.github.ynfeng.commander.engine.executor.NodeExecutors;
import com.github.ynfeng.commander.engine.executor.SPINodeExecutors;

public class EngineModule extends AbstractModule {
    private final EngineConfig engineConfig = new EngineConfig();

    @Override
    public void init() {
        registerComponent(ProcessIdGenerator.class, new UUIDProcessIdGenerator());
        registerComponent(NodeExecutors.class, new SPINodeExecutors());
    }

    public ReactorProcessEngine getEngine() {
        return new ReactorProcessEngine(this);
    }

    @Override
    public String name() {
        return "reactor-engine-module";
    }
}
