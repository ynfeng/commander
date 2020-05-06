package com.github.ynfeng.commander.definition;

public class DecisionDefinition extends AbstractNodeDefinition {
    private final ConditionBranches branches = new ConditionBranches();
    private NodeDefinition next;

    protected DecisionDefinition(String refName) {
        super(refName);
    }

    @Override
    public <T extends NodeDefinition> T next() {
        return (T) next;
    }

    @Override
    public void next(NodeDefinition nodeDefinition) {
        next = nodeDefinition;
    }

    public DecisionDefinition condition(Expression expression, NodeDefinition nodeDefinition) {
        branches.add(expression, nodeDefinition);
        return this;
    }

    public ConditionBranches branches() {
        return branches;
    }
}
