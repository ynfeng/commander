package com.github.ynfeng.commander.definition;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode(of = "expressionValue")
public class Expression {
    private final String expressionValue;

    private Expression(String expressionValue) {
        this.expressionValue = expressionValue;
    }

    public static Expression of(String expressionValue) {
        return new Expression(expressionValue);
    }
}
