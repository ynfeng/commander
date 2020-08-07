package com.github.ynfeng.commander.engine;


import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.github.ynfeng.commander.engine.context.ProcessContext;
import com.github.ynfeng.commander.engine.context.ProcessId;
import com.github.ynfeng.commander.engine.exception.IllegalExpressionException;
import com.github.ynfeng.commander.engine.expression.MVELExpressionEvaluator;
import com.github.ynfeng.commander.definition.ProcessDefinition;
import com.github.ynfeng.commander.definition.StartDefinition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MVELExpressionEvaluatorTest {
    private MVELExpressionEvaluator evaluator;

    @BeforeEach
    public void setup() {
        evaluator = new MVELExpressionEvaluator();
    }

    @Test
    public void should_condition_true() {
        Variables variables = new Variables();
        variables.put("level", 3);
        ProcessDefinition processDefinition = new ProcessDefinition("name", 1);
        processDefinition.firstNode(new StartDefinition());
        ProcessContext context = ProcessContext.create(ProcessId.of("id"), processDefinition);
        context.input(variables);

        boolean condition = evaluator.eval("context.input['level'] == 3", context);

        assertThat(condition, is(true));
    }

    @Test
    public void should_condition_false() {
        Variables variables = new Variables();
        variables.put("level", 3);
        ProcessDefinition processDefinition = new ProcessDefinition("name", 1);
        processDefinition.firstNode(new StartDefinition());
        ProcessContext context = ProcessContext.create(ProcessId.of("id"), processDefinition);
        context.input(variables);

        boolean condition = evaluator.eval("context.input['level'] < 2", context);

        assertThat(condition, is(false));
    }

    @Test
    public void should_throw_exception_when_illegall_expression() {
        Variables variables = new Variables();
        variables.put("level", 3);
        ProcessDefinition processDefinition = new ProcessDefinition("name", 1);
        processDefinition.firstNode(new StartDefinition());
        ProcessContext context = ProcessContext.create(ProcessId.of("id"), processDefinition);
        context.input(variables);

        assertThrows(IllegalExpressionException.class, () -> {
            evaluator.eval("context.level == 2", context);
        });
    }
}
