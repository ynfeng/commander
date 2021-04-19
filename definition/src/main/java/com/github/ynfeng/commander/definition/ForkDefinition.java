package com.github.ynfeng.commander.definition;

public class ForkDefinition extends AbstractNodeDefinition {
    private final ForkBranchs forkBranchs = new ForkBranchs();

    public ForkDefinition(String refName) {
        super(refName);
    }

    public ForkDefinition branch(NodeDefinition firstNode) {
        ForkBranch forkBranch = new ForkBranch(firstNode);
        forkBranchs.add(forkBranch);
        return this;
    }

    public ForkBranchs branchs() {
        return forkBranchs;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ForkDefinition)) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }

        ForkDefinition that = (ForkDefinition) o;

        return forkBranchs.equals(that.forkBranchs);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + forkBranchs.hashCode();
        return result;
    }
}
