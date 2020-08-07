package com.github.ynfeng.commander.core.definition;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode(of = "expressionValue")
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
}
