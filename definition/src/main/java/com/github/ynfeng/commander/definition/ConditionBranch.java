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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ConditionBranch)) {
            return false;
        }

        ConditionBranch that = (ConditionBranch) o;

        if (!expression.equals(that.expression)) {
            return false;
        }
        return fistNode.equals(that.fistNode);
    }

    @Override
    public int hashCode() {
        int result = expression.hashCode();
        result = 31 * result + fistNode.hashCode();
        return result;
    }
}
