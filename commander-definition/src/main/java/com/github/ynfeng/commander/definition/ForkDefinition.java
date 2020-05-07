package com.github.ynfeng.commander.definition;

import com.github.ynfeng.commander.core.definition.AbstractNodeDefinition;
import com.github.ynfeng.commander.core.definition.NodeDefinition;

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
