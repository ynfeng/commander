package com.github.ynfeng.commander.engine;

import com.github.ynfeng.commander.support.env.AbstractEnvironment;
import com.github.ynfeng.commander.support.env.PropertySource;

public class AkkaEngineEnv extends AbstractEnvironment {
    protected AkkaEngineEnv(PropertySource propertySource) {
        super(propertySource);
    }

    @Override
    public String name() {
        return "akka-engine";
    }
}
