package com.github.ynfeng.commander.core.definition;

public class ForkBranch {
    private final NodeDefinition firstNode;

    protected ForkBranch(NodeDefinition firstNode) {
        this.firstNode = firstNode;
    }

    @SuppressWarnings("unchecked")
    public <T extends NodeDefinition> T next() {
        return (T) firstNode;
    }
}
