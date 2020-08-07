package com.github.ynfeng.commander.definition;

public class ConditionBranch {
    public static final ConditionBranch EMPTY = new ConditionBranch(Expression.EMPTY, NodeDefinition.NULL);
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
