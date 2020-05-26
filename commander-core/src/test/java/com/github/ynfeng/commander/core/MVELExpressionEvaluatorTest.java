package com.github.ynfeng.commander.core;


import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.github.ynfeng.commander.core.context.ProcessContext;
import com.github.ynfeng.commander.core.context.ProcessId;
import com.github.ynfeng.commander.core.definition.ProcessDefinition;
import com.github.ynfeng.commander.core.definition.StartDefinition;
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
        Parameters parameters = new Parameters();
        parameters.put("level", 3);
        ProcessDefinition processDefinition = new ProcessDefinition("name", 1);
        processDefinition.firstNode(new StartDefinition());
        ProcessContext context = ProcessContext.create(ProcessId.of("id"), processDefinition);
        context.input(parameters);

        boolean condition = evaluator.eval("context.input['level'] == 3", context);

        assertThat(condition, is(true));
    }

    @Test
    public void should_condition_false() {
        Parameters parameters = new Parameters();
        parameters.put("level", 3);
        ProcessDefinition processDefinition = new ProcessDefinition("name", 1);
        processDefinition.firstNode(new StartDefinition());
        ProcessContext context = ProcessContext.create(ProcessId.of("id"), processDefinition);
        context.input(parameters);

        boolean condition = evaluator.eval("context.input['level'] < 2", context);

        assertThat(condition, is(false));
    }
}
