package com.github.ynfeng.commander.core.expression;

import com.github.ynfeng.commander.core.context.ProcessContext;
import com.github.ynfeng.commander.core.exception.IllegalExpressionException;
import org.mvel2.MVEL;

public class MVELExpressionEvaluator implements ExepressionEvaluator {

    @Override
    public <T> T eval(String expression, ProcessContext context) {
        try {
            return (T) MVEL.eval(expression, new EvalContextWrapper(context));
        } catch (Exception e) {
            throw new IllegalExpressionException(e);
        }
    }

    static class EvalContextWrapper {
        private final ProcessContext context;

        EvalContextWrapper(ProcessContext context) {
            this.context = context;
        }

        public ProcessContext getContext() {
            return context;
        }
    }
}
