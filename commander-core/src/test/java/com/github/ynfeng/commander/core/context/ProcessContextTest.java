package com.github.ynfeng.commander.core.context;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.github.ynfeng.commander.core.definition.ProcessDefinition;
import com.github.ynfeng.commander.core.definition.StartDefinition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ProcessContextTest {
    private ProcessContext processContext;

    @BeforeEach
    public void setUp() {
        ProcessDefinition processDefinition = new ProcessDefinition("test", 1);
        processDefinition.firstNode(new StartDefinition());
        processContext = ProcessContext.create(ProcessId.of("id"), processDefinition);
    }


    @Test
    public void should_be_created_status_when_process_context_create() {
        assertThat(processContext.status(), is(ProcessStatus.CREATED));
    }

    @Test
    public void should_running_status() {
        processContext.running();

        assertThat(processContext.status(), is(ProcessStatus.RUNNING));
    }

    @Test
    public void should_complete_preocess() {
        processContext.complete();

        assertThat(processContext.status(), is(ProcessStatus.COMPLETED));
    }
}
