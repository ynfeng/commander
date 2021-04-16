package com.github.ynfeng.commander.engine;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.github.ynfeng.commander.definition.EndDefinition;
import com.github.ynfeng.commander.definition.ForkDefinition;
import com.github.ynfeng.commander.definition.JoinDefinition;
import com.github.ynfeng.commander.definition.ProcessDefinition;
import com.github.ynfeng.commander.definition.RelationShips;
import com.github.ynfeng.commander.definition.ServiceCoordinate;
import com.github.ynfeng.commander.definition.ServiceDefinition;
import com.github.ynfeng.commander.definition.StartDefinition;
import java.time.Duration;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class ReactorForkJoinNodeExecutorTest extends EngineTestSupport {
    @Test
    void should_execute_fork_node() {
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
        Mockito.when(repository.findProcessDefinition("test", 1))
            .thenReturn(Optional.of(processDefinition));

        ProcessFuture future = engine.startProcess("test", 1)
            .waitNodeStart("aService", Duration.ofMinutes(1))
            .waitNodeStart("otherService", Duration.ofMinutes(1));

        engine.continueProcess(ProcessId.of("1"), "aService", Variables.EMPTY);
        engine.continueProcess(ProcessId.of("1"), "otherService", Variables.EMPTY);
        List<String> executedNodes = future.waitProcessComplete(Duration.ofMinutes(1)).executedNodes();

        assertThat(executedNodes.get(0), is("start"));
        assertThat(executedNodes.get(1), is("aFork"));
        assertThat(executedNodes.get(2), containsString("Service"));
        assertThat(executedNodes.get(3), containsString("Service"));
        assertThat(executedNodes.get(4), is("aJoin"));
        assertThat(executedNodes.get(5), is("end"));
    }

}
