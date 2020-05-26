package com.github.ynfeng.commander.core.expression;

import com.github.ynfeng.commander.core.context.ProcessContext;

public interface ExepressionEvaluator {
    <T> T eval(String expression, ProcessContext context);
}
