package com.github.ynfeng.commander.definition;

public class ForkDefinition extends AbstractNodeDefinition {
    private final ForkBranchs forkBranchs = new ForkBranchs();
    protected ForkDefinition(String refName) {
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
}
