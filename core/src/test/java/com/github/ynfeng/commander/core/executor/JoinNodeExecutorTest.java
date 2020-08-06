package com.github.ynfeng.commander.core.executor;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;

import com.github.ynfeng.commander.core.ProcessEngineTestSupport;
import com.github.ynfeng.commander.core.context.ProcessStatus;
import com.github.ynfeng.commander.core.definition.EndDefinition;
import com.github.ynfeng.commander.core.definition.ForkDefinition;
import com.github.ynfeng.commander.core.definition.JoinDefinition;
import com.github.ynfeng.commander.core.definition.ProcessDefinition;
import com.github.ynfeng.commander.core.definition.RelationShips;
import com.github.ynfeng.commander.core.definition.ServiceCoordinate;
import com.github.ynfeng.commander.core.definition.ServiceDefinition;
import com.github.ynfeng.commander.core.definition.StartDefinition;
import com.github.ynfeng.commander.core.engine.ProcessFuture;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class JoinNodeExecutorTest extends ProcessEngineTestSupport {

    @Test
    public void should_execute_join_node() throws InterruptedException {
        ProcessDefinition processDefinition = ProcessDefinition.builder()
            .withName("test")
            .withVersion(1)
            .withNodes(
                new StartDefinition(),
                new EndDefinition("end"),
                new ServiceDefinition("aService", ServiceCoordinate.of("aService", 1)),
                new ServiceDefinition("otherService", ServiceCoordinate.of("otherService", 1)),
                new ForkDefinition("aFork"),
                new JoinDefinition("aJoin")
            ).withRelationShips(
                RelationShips.builder()
                    .withLink("start", "aFork")
                    .withFork("aFork", "aService", "otherService")
                    .withJoin("aJoin", "aService", "otherService")
                    .withLink("aJoin", "end")
                    .build()
            )
            .build();

        ProcessFuture processFuture = processEngine.startProcess(processDefinition).sync();
        List<String> executedNodes = processFuture.executedNodes();

        assertThat(processFuture.status(), is(ProcessStatus.COMPLETED));
        assertThat(executedNodes.get(0), is("start"));
        assertThat(executedNodes.get(1), is("aFork"));
        assertThat(executedNodes.get(2), is("aService"));
        assertThat(executedNodes.get(3), is("otherService"));
        assertThat(executedNodes.get(4), is("aJoin"));
        assertThat(executedNodes.get(5), is("end"));
    }

    @Override
    protected void mockExtraExecutors(NodeExecutors nodeExecutors) {
        Mockito.when(nodeExecutors.getExecutor(any(StartDefinition.class)))
            .thenReturn(new StartNodeExecutor());
        Mockito.when(nodeExecutors.getExecutor(any(EndDefinition.class)))
            .thenReturn(new EndNodeExecutor());
        Mockito.when(nodeExecutors.getExecutor(any(ServiceDefinition.class)))
            .thenReturn(new ServiceNodeExecutor());
        Mockito.when(nodeExecutors.getExecutor(any(ForkDefinition.class)))
            .thenReturn(new ForkNodeExecutor());
        Mockito.when(nodeExecutors.getExecutor(any(JoinDefinition.class)))
            .thenReturn(new JoinNodeExecutor());
    }
}