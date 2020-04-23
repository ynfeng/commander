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
    public void should_build_with_name_and_version() {
        ProcessDefinition processDefinition = processDefinitionBuilder.build();

        assertThat(processDefinition.name(), is("foo"));
        assertThat(processDefinition.version(), is(1));
    }

    @Test
    public void should_build_with_start_node() {
        ProcessDefinition processDefinition = processDefinitionBuilder
            .start()
            .build();

        assertThat(processDefinition.start(), instanceOf(StartDefinition.class));
    }

    @Test
    public void should_build_with_end_node() {
        ProcessDefinition processDefinition = processDefinitionBuilder
            .start()
            .next(EndDefinition.create())
            .build();

        assertThat(processDefinition.start().next(), instanceOf(EndDefinition.class));
        assertThat(processDefinition.start().next().next(), is(NodeDefinition.EMPTY));
        assertThat(processDefinition.start().next().next().next(), nullValue());
    }
}
