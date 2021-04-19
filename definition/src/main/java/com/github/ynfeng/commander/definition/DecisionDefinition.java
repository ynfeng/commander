package com.github.ynfeng.commander.definition;

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

    @SuppressWarnings("checkstyle:MethodLength")
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DecisionDefinition)) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }

        DecisionDefinition that = (DecisionDefinition) o;

        if (!branches.equals(that.branches)) {
            return false;
        }
        return defaultBranch.equals(that.defaultBranch);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + branches.hashCode();
        result = 31 * result + defaultBranch.hashCode();
        return result;
    }
}
