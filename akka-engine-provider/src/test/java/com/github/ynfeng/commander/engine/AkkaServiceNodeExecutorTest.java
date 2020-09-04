package com.github.ynfeng.commander.engine;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

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

public class AkkaServiceNodeExecutorTest extends EngineTestSupport {
    @Test
    public void should_execute_service_node() throws InterruptedException {
        ProcessDefinition processDefinition = ProcessDefinition.builder()
            .withVersion(1)
            .withName("test")
            .withNodes(
                new StartDefinition(),
                new ServiceDefinition("aService", ServiceCoordinate.of("remote", 1)),
                new EndDefinition("end")
            ).withRelationShips(
                RelationShips.builder()
                    .withLink("start", "aService")
                    .withLink("aService", "end")
                    .build()
            ).build();
        Mockito.when(repository.findProcessDefinition("test", 1))
            .thenReturn(Optional.of(processDefinition));

        try {
            engine.startProcess("test", 1).get(1, TimeUnit.SECONDS);
        } catch (Exception e) {
        }
        ProcessInstanceInfo info = engine.continueProcess(ProcessId.of("1"), "aService", Variables.EMPTY).join();

        List<NodeDefinition> nodeDefinitions = info.executedNodes();
        assertThat(nodeDefinitions.get(0).refName(), is("start"));
        assertThat(nodeDefinitions.get(1).refName(), is("aService"));
        assertThat(nodeDefinitions.get(2).refName(), is("end"));
    }
}
