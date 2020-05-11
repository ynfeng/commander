package com.github.ynfeng.commander.executor;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.github.ynfeng.commander.core.context.ProcessContext;
import com.github.ynfeng.commander.core.context.ProcessId;
import com.github.ynfeng.commander.core.context.ProcessStatus;
import com.github.ynfeng.commander.core.definition.ProcessDefinition;
import com.github.ynfeng.commander.definition.ProcessDefinitionBuilder;
import org.junit.jupiter.api.Test;

class StartNodeExecutorTest extends ProcessEngineTestSupport {

    @Test
    public void should_execute_start_node() {
        ProcessDefinitionBuilder builder = ProcessDefinitionBuilder.create("test", 1);
        builder.createStart();
        ProcessDefinition processDefinition = builder.build();

        ProcessId processId = processEngine.startProcess(processDefinition);
        ProcessContext processContext = processEngine.processContext(processId);

        assertThat(processContext.status(), is(ProcessStatus.RUNNING));
        assertThat(processContext.executedNodes().get(0), is("start"));
    }

}
