package com.github.ynfeng.commander.definition;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.Test;

class ProcessDefinitionTest {

    @Test
    void should_create() {
        ProcessDefinition processDefinition = new ProcessDefinition("aProcess", 1);
        processDefinition.firstNode(new NextableNodeDefinition("aStartNode") {
        });

        assertThat(processDefinition.name(), is("aProcess"));
        assertThat(processDefinition.version(), is(1));
        assertThat(processDefinition.firstNode().refName(), is("aStartNode"));
    }

}
