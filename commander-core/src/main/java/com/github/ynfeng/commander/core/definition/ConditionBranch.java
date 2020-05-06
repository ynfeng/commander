package com.github.ynfeng.commander.core.definition;

public class ConditionBranch {
    private final Expression expression;
    private final NodeDefinition fistNode;

    protected ConditionBranch(Expression expression, NodeDefinition fistNode) {
        this.expression = expression;
        this.fistNode = fistNode;
    }

    public Expression expression() {
        return expression;
    }

    @SuppressWarnings("unchecked")
    public <T extends NodeDefinition> T next() {
        return (T) fistNode;
    }
}
