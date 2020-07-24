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
        processDefinitionBuilder = ProcessDefinition.builder()
            .withName("foo")
            .withVersion(1);
    }

    @Test
    public void should_build_empty_process_definition() {
        processDefinitionBuilder
            .withNodes(new StartDefinition(), new EndDefinition("normalEnd"))
            .withRelationShips(
                RelationShips.builder()
                    .withLink("start", "normalEnd")
                    .build()
            ).build();

        ProcessDefinition processDefinition = processDefinitionBuilder.build();

        assertThat(processDefinition.name(), is("foo"));
        assertThat(processDefinition.version(), is(1));
        assertThat(processDefinition.firstNode().refName(), is("start"));
        assertThat(((StartDefinition) processDefinition.firstNode()).next().refName(), is("normalEnd"));
    }

    @Test
    public void should_throw_exception_when_link_with_not_exists_source_node() {
        processDefinitionBuilder.withRelationShips(
            RelationShips.builder()
                .withLink("start", "end")
                .build());
        ProcessDefinitionException exception = assertThrows(ProcessDefinitionException.class, () -> {
            processDefinitionBuilder.build();
        });

        assertThat(exception.getMessage(), is("The \"start\" node definition not exists."));
    }

    @Test
    public void should_throw_exception_when_link_with_not_exists_target_node() {
        processDefinitionBuilder
            .withNodes(new StartDefinition())
            .withRelationShips(
                RelationShips.builder()
                    .withLink("start", "end")
                    .build()
            );
        ProcessDefinitionException exception = assertThrows(ProcessDefinitionException.class, () -> {
            processDefinitionBuilder.build();
        });

        assertThat(exception.getMessage(), is("The \"end\" node definition not exists."));
    }

    @Test
    public void should_throw_exception_when_duplicate_ref_name() {
        ProcessDefinitionException exception = assertThrows(ProcessDefinitionException.class, () -> {
            processDefinitionBuilder
                .withNodes(
                    new EndDefinition("end"),
                    new EndDefinition("end")
                );
        });

        assertThat(exception.getMessage(), is("The ref name \"end\" was duplicated"));
    }

    @Test
    public void should_build_with_service_definition() {
        ProcessDefinition processDefinition = ProcessDefinition.builder()
            .withName("test")
            .withVersion(1)
            .withNodes(
                new StartDefinition(),
                new EndDefinition("end"),
                new ServiceDefinition("serviceRefName", ServiceCoordinate.of("aService", 1))
            )
            .withRelationShips(
                RelationShips.builder()
                    .withLink("start", "serviceRefName")
                    .withLink("serviceRefName", "end")
                    .build()
            )
            .build();

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
        ProcessDefinition processDefinition = processDefinitionBuilder
            .withNodes(
                new StartDefinition(),
                new ServiceDefinition("refName", ServiceCoordinate.of("aService", 1)),
                new ServiceDefinition("refName1", ServiceCoordinate.of("otherService", 1)),
                new EndDefinition("end")
            ).withRelationShips(
                RelationShips.builder()
                    .withLink("start", "refName")
                    .withLink("refName", "refName1")
                    .withLink("refName1", "end")
                    .build()
            ).build();

        ServiceDefinition refNameServiceDefinition = ((StartDefinition) processDefinition.firstNode()).next();
        ServiceDefinition refName1ServiceDefinition = refNameServiceDefinition.next();

        assertThat(refNameServiceDefinition.refName(), is("refName"));
        assertThat(refName1ServiceDefinition.refName(), is("refName1"));
        assertThat(refName1ServiceDefinition.next().refName(), is("end"));
    }

    @Test
    public void should_build_with_decision_definition() {
        ProcessDefinition processDefinition = processDefinitionBuilder.withNodes(
            new StartDefinition(),
            new ServiceDefinition("aService", ServiceCoordinate.of("aService", 1)),
            new ServiceDefinition("lastService", ServiceCoordinate.of("lastService", 1)),
            new DecisionDefinition("aDecision"),
            new ServiceDefinition("otherService", ServiceCoordinate.of("otherService", 1)),
            new ServiceDefinition("defaultService", ServiceCoordinate.of("defaultService", 1)),
            new EndDefinition("end")
        ).withRelationShips(
            RelationShips.builder()
                .withLink("start", "aService")
                .withLink("aService", "aDecision")
                .withDecision("aDecision", "aService.result.success == true", "otherService")
                .withDefaultDecision("aDecision", "defaultService")
                .withLink("otherService", "lastService")
                .withLink("lastService", "end")
                .withLink("defaultService", "end")
                .build()
        ).build();

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
        ProcessDefinition processDefinition =
            ProcessDefinition.builder()
                .withName("test")
                .withVersion(1)
                .withNodes(
                    new StartDefinition(),
                    new EndDefinition("end"),
                    new ServiceDefinition("aService", ServiceCoordinate.of("aService", 1)),
                    new ServiceDefinition("otherService", ServiceCoordinate.of("otherService", 1)),
                    new ForkDefinition("aFork"),
                    new JoinDefinition("aJoin")
                )
                .withRelationShips(
                    RelationShips.builder()
                        .withLink("start", "aFork")
                        .withFork("aFork", "aService", "otherService")
                        .withJoin("aJoin", "aService", "otherService")
                        .withLink("aJoin", "end")
                        .build()
                )
                .build();

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
