package com.github.ynfeng.commander.core.definition;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.Test;

class ProcessDefinitionTest {

    @Test
    public void should_create() {
        ProcessDefinition processDefinition = new ProcessDefinition("aProcess",1);
        processDefinition.start(new NextableNodeDefinition("aStartNode") {
        });

        assertThat(processDefinition.name(), is("aProcess"));
        assertThat(processDefinition.version(), is(1));
        assertThat(processDefinition.start().refName(), is("aStartNode"));
    }

}
