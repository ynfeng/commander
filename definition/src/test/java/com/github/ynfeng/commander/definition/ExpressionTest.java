package com.github.ynfeng.commander.definition;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.Test;

class ExpressionTest {

    @Test
    public void should_to_string() {
        Expression expression = Expression.of("1 == 1");

        assertThat(expression.toString(), is("1 == 1"));
    }

    @Test
    public void should_not_equals_with_null() {
        Expression expression = Expression.of("1 == 1");
        assertThat(expression.equals(null), is(false));
    }

    @Test
    public void should_equals_with_same_instance() {
        Expression expression = Expression.of("1 == 1");

        assertThat(expression, is(expression));
    }

    @Test
    public void should_not_equals_with_not_same_type() {
        Expression expression = Expression.of("1 == 1");

        assertThat(expression.equals("s"), is(false));
    }

    @Test
    public void should_equals_with_same_expression() {
        Expression expression1 = Expression.of("1 == 1");
        Expression expression2 = Expression.of("1 == 1");

        assertThat(expression1, is(expression2));
    }

    @Test
    public void should_0_hash_code_when_expression_is_null() {
        Expression expression = Expression.of(null);
        assertThat(expression.hashCode(), is(0));
    }

    @Test
    public void should_same_hash_code_when_same_expression() {
        Expression expression1 = Expression.of(" 1== 1");
        Expression expression2 = Expression.of(" 1== 1");

        assertThat(expression1.hashCode(), is(expression2.hashCode()));
    }
}
