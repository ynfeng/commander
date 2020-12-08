package com.github.ynfeng.commander.definition;

import java.util.Objects;

public class Expression {
    public static final Expression EMPTY = of("");
    private final String expressionValue;

    private Expression(String expressionValue) {
        this.expressionValue = expressionValue;
    }

    public static Expression of(String expressionValue) {
        return new Expression(expressionValue);
    }

    @Override
    public String toString() {
        return expressionValue;
    }

    @SuppressWarnings("checkstyle:MethodLength")
    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (this == o) {
            return true;
        }
        if (!(o instanceof Expression)) {
            return false;
        }
        return Objects.equals(expressionValue, ((Expression) o).expressionValue);
    }

    @Override
    public int hashCode() {
        if (expressionValue == null) {
            return 0;
        }
        return expressionValue.hashCode();
    }
}
