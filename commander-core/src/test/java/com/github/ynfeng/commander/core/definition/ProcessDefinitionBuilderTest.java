package com.github.ynfeng.commander.core.definition;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.github.ynfeng.commander.core.exception.ProcessDefinitionException;
import java.util.Iterator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


public class ProcessDefinitionBuilderTest {
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
        assertThat(processDefinition.firstNode().refName(), is("start"));
        assertThat(((StartDefinition) processDefinition.firstNode()).next().refName(), is("normalEnd"));
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

        ServiceDefinition serviceDefinition = ((StartDefinition) processDefinition.firstNode()).next();
        ServiceCoordinate serviceCoordinate = serviceDefinition.serviceCoordinate();

        assertThat(serviceDefinition, instanceOf(ServiceDefinition.class));
        assertThat(serviceCoordinate.name(), is("aService"));
        assertThat(serviceCoordinate.version(), is(1));
        assertThat(serviceDefinition.refName(), is("serviceRefName"));
        assertThat(serviceDefinition.next().refName(), is("end"));
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

        ServiceDefinition refNameServiceDefinition = ((StartDefinition) processDefinition.firstNode()).next();
        ServiceDefinition refName1ServiceDefinition = refNameServiceDefinition.next();

        assertThat(refNameServiceDefinition.refName(), is("refName"));
        assertThat(refName1ServiceDefinition.refName(), is("refName1"));
        assertThat(refName1ServiceDefinition.next().refName(), is("end"));
    }

    @Test
    public void should_build_with_decision_definition() {
        processDefinitionBuilder.createStart();
        processDefinitionBuilder.createEnd("end");
        processDefinitionBuilder.createService("aService", ServiceCoordinate.of("aService", 1));
        processDefinitionBuilder.createService("lastService", ServiceCoordinate.of("lastService", 1));
        processDefinitionBuilder.createDecision("aDecision")
            .condition(Expression.of("aService.result.success == true"),
                processDefinitionBuilder.createService("otherService", ServiceCoordinate.of("otherService", 1)))
            .defaultCondition(processDefinitionBuilder.createService("defaultService", ServiceCoordinate.of("defaultService", 1)));
        processDefinitionBuilder.link("start", "aService");
        processDefinitionBuilder.link("aService", "aDecision");
        processDefinitionBuilder.link("otherService", "lastService");
        processDefinitionBuilder.link("lastService", "end");
        ProcessDefinition processDefinition = processDefinitionBuilder.build();

        ServiceDefinition aServiceDefinition = ((StartDefinition) processDefinition.firstNode()).next();
        DecisionDefinition decisionDefinition = aServiceDefinition.next();
        ConditionBranches branches = decisionDefinition.branches();
        Iterator<ConditionBranch> branchesIterator = branches.iterator();
        ConditionBranch branch = branchesIterator.next();
        ServiceDefinition otherService = branch.next();
        ServiceDefinition lastService = otherService.next();

        assertThat(decisionDefinition.defaultCondition().next().refName(), is("defaultService"));
        assertThat(branches.size(), is(1));
        assertThat(branch.expression(), is(Expression.of("aService.result.success == true")));
        assertThat(otherService.refName(), is("otherService"));
        assertThat(lastService.refName(), is("lastService"));
        assertThat(lastService.next().refName(), is("end"));
    }

    @Test
    public void should_build_with_fork_and_join_definition() {
        processDefinitionBuilder.createStart();
        processDefinitionBuilder.createEnd("end");
        processDefinitionBuilder.createFork("aFork")
            .branch(processDefinitionBuilder.createService("aService", ServiceCoordinate.of("aService", 1)))
            .branch(processDefinitionBuilder.createService("otherService", ServiceCoordinate.of("otherService", 1)));
        processDefinitionBuilder.createJoin("aJoin").on("aService", "otherService");
        processDefinitionBuilder.link("start", "aFork");
        processDefinitionBuilder.link("aJoin", "end");
        ProcessDefinition processDefinition = processDefinitionBuilder.build();

        ForkDefinition fork = ((StartDefinition) processDefinition.firstNode()).next();
        ForkBranchs forkBranchs = fork.branchs();
        Iterator<ForkBranch> forkBranchIterator = forkBranchs.iterator();
        ForkBranch branch1 = forkBranchIterator.next();
        ForkBranch branch2 = forkBranchIterator.next();
        ServiceDefinition aService = branch1.next();
        ServiceDefinition otherService = branch2.next();
        JoinDefinition joinDefinition = aService.next();
        assertThat(aService.refName(), is("aService"));
        assertThat(otherService.refName(), is("otherService"));
        assertThat(aService.next().refName(), is("aJoin"));
        assertThat(otherService.next().refName(), is("aJoin"));
        assertThat(joinDefinition, sameInstance(otherService.next()));
        assertThat(joinDefinition.next().refName(), is("end"));
    }
}
