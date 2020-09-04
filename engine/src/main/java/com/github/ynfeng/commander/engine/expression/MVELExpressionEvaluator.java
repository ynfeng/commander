package com.github.ynfeng.commander.engine.expression;

import com.github.ynfeng.commander.engine.ProcessInstance;
import org.mvel2.MVEL;

public class MVELExpressionEvaluator implements ExepressionEvaluator {

    @Override
    public <T> T eval(String expression, ProcessInstance processInstance) {
        try {
            return (T) MVEL.eval(expression, new EvalContextWrapper(processInstance));
        } catch (Exception e) {
            throw new IllegalExpressionException(e);
        }
    }

    static class EvalContextWrapper {
        private final ProcessInstance context;

        EvalContextWrapper(ProcessInstance processInstance) {
            context = processInstance;
        }

        public ProcessInstance getContext() {
            return context;
        }
    }
}
