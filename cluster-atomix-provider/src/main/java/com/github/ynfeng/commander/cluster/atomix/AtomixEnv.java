package com.github.ynfeng.commander.cluster.atomix;

import com.github.ynfeng.commander.support.env.AbstractEnvironment;
import com.github.ynfeng.commander.support.env.PropertySource;

public class AtomixEnv extends AbstractEnvironment {

    protected AtomixEnv(PropertySource propertySource) {
        super(propertySource);
    }

    @Override
    public String name() {
        return "atomix";
    }
}
