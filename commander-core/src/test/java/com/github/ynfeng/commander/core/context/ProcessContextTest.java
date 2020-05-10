package com.github.ynfeng.commander.core.context;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.github.ynfeng.commander.core.definition.NodeDefinition;
import com.github.ynfeng.commander.core.definition.ProcessDefinition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ProcessContextTest {
    private ProcessContext processContext;

    @BeforeEach
    public void setUp(){
        processContext = new ProcessContext(ProcessId.of("id"), new ProcessDefinition("test", 1));
    }

    @Test
    public void should_be_created_status() {
        assertThat(processContext.status(), is(ProcessStatus.CREATED));
    }

    @Test
    public void should_running(){
        processContext.running();

        assertThat(processContext.status(), is(ProcessStatus.RUNNING));
    }

    @Test
    public void should_add_exeucted_node(){
        processContext.addExecutedNode("start");

        assertThat(processContext.executedNodes().get(0), is("start"));
    }

    @Test
    public void should_next_node(){
        processContext.nextNode(NodeDefinition.NULL);

        assertThat(processContext.currentNode(), is(NodeDefinition.NULL));
    }
}
