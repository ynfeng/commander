package com.github.ynfeng.commander.core;

import com.github.ynfeng.commander.core.context.ProcessContext;
import org.mvel2.MVEL;

public class MVELExpressionEvaluator implements ExepressionEvaluator {

    @Override
    public <T> T eval(String expression, ProcessContext context) {
        return (T) MVEL.eval(expression, new EvalContextWrapper(context));
    }

    class EvalContextWrapper {
        private final ProcessContext context;

        EvalContextWrapper(ProcessContext context) {
            this.context = context;
        }

        public ProcessContext getContext() {
            return context;
        }
    }
}
