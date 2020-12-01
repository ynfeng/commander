package com.github.ynfeng.commander.definition;

import java.util.Objects;

public abstract class AbstractNodeDefinition implements NodeDefinition {
    private final String refName;

    protected AbstractNodeDefinition(String refName) {
        this.refName = refName;
    }

    @Override
    public String refName() {
        return refName;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (getClass() != o.getClass()) {
            return false;
        }
        AbstractNodeDefinition that = (AbstractNodeDefinition) o;
        return Objects.equals(refName, that.refName);
    }

    @Override
    public int hashCode() {
        if (refName != null) {
            return refName.hashCode();
        }
        return 0;
    }
}
