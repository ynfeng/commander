package com.github.ynfeng.commander.core.definition;

public class DecisionDefinition extends AbstractNodeDefinition {
    private final ConditionBranches branches = new ConditionBranches();
    private ConditionBranch defaultBranch = ConditionBranch.EMPTY;

    public DecisionDefinition(String refName) {
        super(refName);
    }

    public DecisionDefinition condition(Expression expression, NodeDefinition nodeDefinition) {
        branches.add(expression, nodeDefinition);
        return this;
    }

    public ConditionBranches branches() {
        return branches;
    }

    public DecisionDefinition defaultCondition(NodeDefinition nodeDefinition) {
        defaultBranch = new ConditionBranch(Expression.EMPTY, nodeDefinition);
        return this;
    }

    public ConditionBranch defaultCondition() {
        return defaultBranch;
    }
}
