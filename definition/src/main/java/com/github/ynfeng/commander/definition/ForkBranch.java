package com.github.ynfeng.commander.definition;

public class ForkBranch {
    private final NodeDefinition firstNode;

    protected ForkBranch(NodeDefinition firstNode) {
        this.firstNode = firstNode;
    }

    @SuppressWarnings("unchecked")
    public <T extends NodeDefinition> T next() {
        return (T) firstNode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ForkBranch)) {
            return false;
        }

        ForkBranch that = (ForkBranch) o;

        return firstNode.equals(that.firstNode);
    }

    @Override
    public int hashCode() {
        return firstNode.hashCode();
    }
}
