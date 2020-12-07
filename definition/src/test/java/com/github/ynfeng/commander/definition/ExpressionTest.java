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
}
