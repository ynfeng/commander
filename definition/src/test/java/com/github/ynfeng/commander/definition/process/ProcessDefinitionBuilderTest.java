package com.github.ynfeng.commander.definition.process;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import com.github.ynfeng.commander.definition.node.EndDefinition;
import com.github.ynfeng.commander.definition.node.NodeDefinition;
import com.github.ynfeng.commander.definition.node.StartDefinition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


public class ProcessDefinitionBuilderTest {

    private ProcessDefinitionBuilder processDefinitionBuilder;

    @BeforeEach
    public void setup() {
        processDefinitionBuilder = ProcessDefinitionBuilder.create("foo", 1);
    }

    @Test
    public void should_build_empty_process_definition() {
        ProcessDefinition processDefinition = processDefinitionBuilder
            .start()
            .end()
            .build();

        assertThat(processDefinition.name(), is("foo"));
        assertThat(processDefinition.version(), is(1));
        assertThat(processDefinition.start(), instanceOf(StartDefinition.class));
        assertThat(processDefinition.start().next(), instanceOf(EndDefinition.class));
        assertThat(processDefinition.start().next().next(), is(NodeDefinition.EMPTY));
        assertThat(processDefinition.start().next().next().next(), nullValue());
    }
}
