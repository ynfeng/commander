package com.github.ynfeng.commander.core.context;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.github.ynfeng.commander.core.definition.NodeDefinition;
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
        processContext = new ProcessContext(ProcessId.of("id"), processDefinition);
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
        processContext.done();

        assertThat(processContext.status(), is(ProcessStatus.COMPLETED));
    }

    @Test
    public void should_complete_current_node() {
        processContext.completeCurrentNode(NodeDefinition.NULL);

        assertThat(processContext.executedNodes().get(0), is("start"));
    }
}
