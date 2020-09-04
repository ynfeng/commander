package com.github.ynfeng.commander.engine.expression;

import com.github.ynfeng.commander.engine.ProcessInstance;

public interface ExepressionEvaluator {
    <T> T eval(String expression, ProcessInstance processInstance);
}
