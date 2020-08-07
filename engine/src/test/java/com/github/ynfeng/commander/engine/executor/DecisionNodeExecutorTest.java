package com.github.ynfeng.commander.engine.executor;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;

import com.github.ynfeng.commander.engine.ProcessEngineTestSupport;
import com.github.ynfeng.commander.engine.Variables;
import com.github.ynfeng.commander.engine.context.ProcessStatus;
import com.github.ynfeng.commander.engine.engine.ProcessFuture;
import com.github.ynfeng.commander.definition.DecisionDefinition;
import com.github.ynfeng.commander.definition.EndDefinition;
import com.github.ynfeng.commander.definition.ProcessDefinition;
import com.github.ynfeng.commander.definition.RelationShips;
import com.github.ynfeng.commander.definition.ServiceCoordinate;
import com.github.ynfeng.commander.definition.ServiceDefinition;
import com.github.ynfeng.commander.definition.StartDefinition;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class DecisionNodeExecutorTest extends ProcessEngineTestSupport {

    @Test
    public void should_execute_decision_node() throws InterruptedException {
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

        Variables variables = new Variables();
        variables.put("level", "1");
        ProcessFuture future = processEngine.startProcess(processDefinition, variables).sync();

        List<String> executedNodes = future.executedNodes();
        assertThat(executedNodes.get(2), is("aService"));
        assertThat(executedNodes.get(3), is("bService"));
        assertThat(future.status(), is(ProcessStatus.COMPLETED));
    }

    @Test
    public void should_execute_default_branch_when_no_suitable_condition() throws InterruptedException {
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
                    .withDefaultDecision("decision","defaultService")
                    .withLink("defaultService","end")
                    .withLink("aService","bService")
                    .withLink("bService","end")
                    .build()
            ).build();

        Variables variables = new Variables();
        variables.put("level", "1");
        ProcessFuture future = processEngine.startProcess(processDefinition).sync();

        List<String> executedNodes = future.executedNodes();
        assertThat(executedNodes.get(2), is("defaultService"));
        assertThat(future.status(), is(ProcessStatus.COMPLETED));
    }

    @Override
    protected void mockExtraExecutors(NodeExecutors nodeExecutors) {
        Mockito.when(nodeExecutors.getExecutor(any(StartDefinition.class)))
            .thenReturn(new StartNodeExecutor());
        Mockito.when(nodeExecutors.getExecutor(any(EndDefinition.class)))
            .thenReturn(new EndNodeExecutor());
        Mockito.when(nodeExecutors.getExecutor(any(ServiceDefinition.class)))
            .thenReturn(new ServiceNodeExecutor());
        Mockito.when(nodeExecutors.getExecutor(any(DecisionDefinition.class)))
            .thenReturn(new DecisionNodeExecutor());
    }
}
