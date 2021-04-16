package com.github.ynfeng.commander.engine;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.github.ynfeng.commander.definition.DecisionDefinition;
import com.github.ynfeng.commander.definition.EndDefinition;
import com.github.ynfeng.commander.definition.ProcessDefinition;
import com.github.ynfeng.commander.definition.RelationShips;
import com.github.ynfeng.commander.definition.ServiceCoordinate;
import com.github.ynfeng.commander.definition.ServiceDefinition;
import com.github.ynfeng.commander.definition.StartDefinition;
import java.time.Duration;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class ReactorDecisionNodeExecutorTest extends EngineTestSupport {

    @RepeatedTest(200)
    void should_execute_decision_node() {
        ProcessDefinition processDefinition = ProcessDefinition.builder()
            .withName("test")
            .withVersion(1)
            .withNodes(
                new StartDefinition(),
                new EndDefinition("end"),
                new ServiceDefinition("defaultService", ServiceCoordinate.of("defaultService", 1)),
                new ServiceDefinition("aService", ServiceCoordinate.of("aService", 1)),
                new ServiceDefinition("bService", ServiceCoordinate.of("bService", 1)),
                new DecisionDefinition("decision")
            ).withRelationShips(
                RelationShips.builder()
                    .withLink("start", "decision")
                    .withDecision("decision", "context.input['level'] == 1", "aService")
                    .withDefaultDecision("decision", "defaultService")
                    .withLink("aService", "bService")
                    .withLink("bService", "end")
                    .build()
            ).build();
        Mockito.when(repository.findProcessDefinition("test", 1))
            .thenReturn(Optional.of(processDefinition));

        Variables variables = new Variables();
        variables.put("level", "1");
        engine.startProcess("test", 1, variables).waitNodeComplete("decision", Duration.ofMinutes(1));
        engine.continueProcess(ProcessId.of("1"), "aService", Variables.EMPTY).waitNodeComplete("aService", Duration.ofMinutes(1));
        List<String> executedNodes = engine.continueProcess(ProcessId.of("1"), "bService", Variables.EMPTY)
            .waitProcessComplete(Duration.ofMinutes(1))
            .executedNodes();
        assertThat(executedNodes.get(2), is("aService"));
        assertThat(executedNodes.get(3), is("bService"));
    }

    @Test
    void should_execute_default_branch_when_no_suitable_condition() {
        ProcessDefinition processDefinition = ProcessDefinition.builder()
            .withName("test")
            .withVersion(1)
            .withNodes(
                new StartDefinition(),
                new EndDefinition("end"),
                new DecisionDefinition("decision"),
                new ServiceDefinition("aService", ServiceCoordinate.of("aService", 1)),
                new ServiceDefinition("bService", ServiceCoordinate.of("bService", 1)),
                new ServiceDefinition("defaultService", ServiceCoordinate.of("defaultService", 1))
            )
            .withRelationShips(
                RelationShips.builder()
                    .withLink("start", "decision")
                    .withDecision("decision", "context.input['level'] == 2", "aService")
                    .withDefaultDecision("decision", "defaultService")
                    .withLink("defaultService", "end")
                    .withLink("aService", "bService")
                    .withLink("bService", "end")
                    .build()
            ).build();
        Mockito.when(repository.findProcessDefinition("test", 1))
            .thenReturn(Optional.of(processDefinition));

        Variables variables = new Variables();
        variables.put("level", "1");

        engine.startProcess("test", 1, variables).waitNodeComplete("decision", Duration.ofMinutes(1));
        List<String> executedNodes = engine.continueProcess(ProcessId.of("1"), "defaultService", Variables.EMPTY)
            .waitProcessComplete(Duration.ofMinutes(1))
            .executedNodes();
        assertThat(executedNodes.get(2), is("defaultService"));
    }
}
