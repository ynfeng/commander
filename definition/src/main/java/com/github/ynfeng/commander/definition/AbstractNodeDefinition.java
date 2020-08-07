package com.github.ynfeng.commander.definition;

public abstract class AbstractNodeDefinition implements NodeDefinition {
    private final String refName;

    protected AbstractNodeDefinition(String refName) {
        this.refName = refName;
    }

    @Override
    public String refName() {
        return refName;
    }
}
