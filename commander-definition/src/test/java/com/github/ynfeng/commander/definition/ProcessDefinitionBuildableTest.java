package com.github.ynfeng.commander.definition;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


public class ProcessDefinitionBuildableTest {

    private StartDefinitionBuilder startDefinitionBuilder;

    @BeforeEach
    public void setUp() {
        startDefinitionBuilder = ProcessDefinitionBuilder.create("foo", 1);
    }

    @Test
    public void should_build_empty_process_definition() {
        ProcessDefinition processDefinition = startDefinitionBuilder
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

    @Test
    public void should_build_with_service_process_definition() {
        ProcessDefinition processDefinition = startDefinitionBuilder
            .start()
            .service("refName", ServiceCoordinate.of("aService", 1))
            .end()
            .build();

        ServiceDefinition serviceDefinition = processDefinition.start().next();
        ServiceCoordinate serviceCoordinate = serviceDefinition.serviceCoordinate();
        assertThat(serviceDefinition, instanceOf(ServiceDefinition.class));
        assertThat(serviceCoordinate.name(), is("aService"));
        assertThat(serviceCoordinate.version(), is(1));
        assertThat(serviceDefinition.refName(), is("refName"));
        assertThat(serviceDefinition.next(), instanceOf(EndDefinition.class));
    }
}
