package com.github.ynfeng.commander.engine.expression;

import com.github.ynfeng.commander.engine.context.ProcessContext;

public interface ExepressionEvaluator {
    <T> T eval(String expression, ProcessContext context);
}
