package com.github.ynfeng.commander.definition;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Iterator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


public class ProcessDefinitionBuildableTest {
    private ProcessDefinitionBuilder processDefinitionBuilder;

    @BeforeEach
    public void setUp() {
        processDefinitionBuilder = ProcessDefinitionBuilder.create("foo", 1);
    }

    @Test
    public void should_build_empty_process_definition() {
        processDefinitionBuilder.createStart();
        processDefinitionBuilder.createEnd("normalEnd");
        processDefinitionBuilder.link("start", "normalEnd");

        ProcessDefinition processDefinition = processDefinitionBuilder.build();

        assertThat(processDefinition.name(), is("foo"));
        assertThat(processDefinition.version(), is(1));
        assertThat(processDefinition.start(), instanceOf(StartDefinition.class));
        assertThat(processDefinition.start().next(), instanceOf(EndDefinition.class));
    }

    @Test
    public void should_throw_exception_when_link_with_not_exists_source_node() {
        processDefinitionBuilder.createEnd("end");
        ProcessDefinitionException exception = assertThrows(ProcessDefinitionException.class, () -> {
            processDefinitionBuilder.link("start", "end");
        });

        assertThat(exception.getMessage(), is("The \"start\" node definition not exists."));
    }

    @Test
    public void should_throw_exception_when_link_with_not_exists_target_node() {
        processDefinitionBuilder.createStart();
        ProcessDefinitionException exception = assertThrows(ProcessDefinitionException.class, () -> {
            processDefinitionBuilder.link("start", "end");
        });

        assertThat(exception.getMessage(), is("The \"end\" node definition not exists."));
    }

    @Test
    public void should_throw_exception_when_duplicate_ref_name() {
        ProcessDefinitionException exception = assertThrows(ProcessDefinitionException.class, () -> {
            processDefinitionBuilder.createEnd("end");
            processDefinitionBuilder.createEnd("end");
        });

        assertThat(exception.getMessage(), is("The ref name \"end\" was duplicated"));
    }

    @Test
    public void should_build_with_service_definition() {
        processDefinitionBuilder.createStart();
        processDefinitionBuilder.createEnd("end");
        processDefinitionBuilder.createService("serviceRefName", ServiceCoordinate.of("aService", 1));
        processDefinitionBuilder.link("start", "serviceRefName");
        processDefinitionBuilder.link("serviceRefName", "end");
        ProcessDefinition processDefinition = processDefinitionBuilder.build();

        ServiceDefinition serviceDefinition = processDefinition.start().next();
        ServiceCoordinate serviceCoordinate = serviceDefinition.serviceCoordinate();
        assertThat(serviceDefinition, instanceOf(ServiceDefinition.class));
        assertThat(serviceCoordinate.name(), is("aService"));
        assertThat(serviceCoordinate.version(), is(1));
        assertThat(serviceDefinition.refName(), is("serviceRefName"));
        assertThat(serviceDefinition.next(), instanceOf(EndDefinition.class));
    }

    @Test
    public void should_build_with_multiple_service_definition() {
        processDefinitionBuilder.createStart();
        processDefinitionBuilder.createEnd("end");
        processDefinitionBuilder.createService("refName", ServiceCoordinate.of("aService", 1));
        processDefinitionBuilder.createService("refName1", ServiceCoordinate.of("otherService", 1));
        processDefinitionBuilder
            .link("start", "refName")
            .link("refName", "refName1")
            .link("refName1", "end");
        ProcessDefinition processDefinition = processDefinitionBuilder.build();

        ServiceDefinition refNameServiceDefinition = processDefinition.start().next();
        ServiceDefinition refName1ServiceDefinition = refNameServiceDefinition.next();

        assertThat(refNameServiceDefinition.refName(), is("refName"));
        assertThat(refName1ServiceDefinition.refName(), is("refName1"));
        assertThat(refName1ServiceDefinition.next(), instanceOf(EndDefinition.class));
    }

    @Test
    public void should_build_with_decision_definition() {
        processDefinitionBuilder.createStart();
        processDefinitionBuilder.createEnd("end");
        processDefinitionBuilder.createService("aService", ServiceCoordinate.of("aService", 1));
        processDefinitionBuilder.createService("lastService", ServiceCoordinate.of("lastService", 1));
        processDefinitionBuilder.createDecision("aDecision")
            .condition(Expression.of("aService.result.success == true"),
                processDefinitionBuilder.createService("otherService", ServiceCoordinate.of("otherService", 1)));
        processDefinitionBuilder.link("start", "aService");
        processDefinitionBuilder.link("aService", "aDecision");
        processDefinitionBuilder.link("otherService", "lastService");
        processDefinitionBuilder.link("lastService", "end");
        ProcessDefinition processDefinition = processDefinitionBuilder.build();

        ServiceDefinition aServiceDefinition = processDefinition.start().next();
        DecisionDefinition decisionDefinition = aServiceDefinition.next();

        ConditionBranches branches = decisionDefinition.branches();
        Iterator<ConditionBranch> branchesIterator = branches.iterator();
        ConditionBranch branch = branchesIterator.next();
        ServiceDefinition otherService = branch.next();
        ServiceDefinition lastService = otherService.next();
        assertThat(branches.size(), is(1));
        assertThat(branch.expression(), is(Expression.of("aService.result.success == true")));
        assertThat(otherService.refName(), is("otherService"));
        assertThat(lastService.refName(), is("lastService"));
        assertThat(lastService.next(), instanceOf(EndDefinition.class));
    }
}
