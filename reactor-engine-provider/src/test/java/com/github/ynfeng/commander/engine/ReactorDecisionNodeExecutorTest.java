package com.github.ynfeng.commander.engine;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.github.ynfeng.commander.definition.DecisionDefinition;
import com.github.ynfeng.commander.definition.EndDefinition;
import com.github.ynfeng.commander.definition.NodeDefinition;
import com.github.ynfeng.commander.definition.ProcessDefinition;
import com.github.ynfeng.commander.definition.RelationShips;
import com.github.ynfeng.commander.definition.ServiceCoordinate;
import com.github.ynfeng.commander.definition.ServiceDefinition;
import com.github.ynfeng.commander.definition.StartDefinition;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class ReactorDecisionNodeExecutorTest extends EngineTestSupport {
    @Test
    public void should_execute_decision_node() {
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
        try {
            engine.startProcess("test", 1, variables).get(1, TimeUnit.SECONDS);
        } catch (Exception e) {
        }
        try {
            engine.continueProcess(ProcessId.of("1"), "aService", Variables.EMPTY).getProcessFuture().get(1,TimeUnit.SECONDS);
        } catch (Exception e) {
        }
        ProcessInstanceResult info = engine.continueProcess(ProcessId.of("1"), "bService", Variables.EMPTY).getProcessFuture().join();
        List<NodeDefinition> executedNodes = info.executedNodes();
        assertThat(executedNodes.get(2).refName(), is("aService"));
        assertThat(executedNodes.get(3).refName(), is("bService"));
    }

    @Test
    public void should_execute_default_branch_when_no_suitable_condition() {
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

        try {
            engine.startProcess("test", 1, variables).get(1, TimeUnit.SECONDS);
        } catch (Exception e) {
        }
        ProcessInstanceResult info = engine.continueProcess(ProcessId.of("1"), "defaultService", Variables.EMPTY).getProcessFuture().join();
        List<NodeDefinition> executedNodes = info.executedNodes();
        assertThat(executedNodes.get(2).refName(), is("defaultService"));
    }
}
