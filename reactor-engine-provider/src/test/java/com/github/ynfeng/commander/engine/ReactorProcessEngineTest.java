package com.github.ynfeng.commander.engine;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

import com.github.ynfeng.commander.definition.EndDefinition;
import com.github.ynfeng.commander.definition.ProcessDefinition;
import com.github.ynfeng.commander.definition.RelationShips;
import com.github.ynfeng.commander.definition.ServiceCoordinate;
import com.github.ynfeng.commander.definition.ServiceDefinition;
import com.github.ynfeng.commander.definition.StartDefinition;
import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class ReactorProcessEngineTest extends EngineTestSupport {
    @AfterEach
    public void destory() {
        engine.shutdown();
    }

    @Test
    public void should_start_process() {
        ProcessDefinition processDefinition = ProcessDefinition.builder()
            .withVersion(1)
            .withName("test")
            .withNodes(
                new StartDefinition(),
                new EndDefinition("end")
            ).withRelationShips(
                RelationShips.builder()
                    .withLink("start", "end")
                    .build()
            ).build();

        Mockito.when(repository.findProcessDefinition("test", 1))
            .thenReturn(Optional.of(processDefinition));

        List<String> executedNodes = engine.startProcess("test", 1)
            .waitProcessComplete(Duration.ofMinutes(1))
            .executedNodes();

        assertThat(executedNodes.get(0), is("start"));
        assertThat(executedNodes.get(1), is("end"));
    }

    @Test
    public void should_get_process_id_after_start_process() {
        ProcessDefinition processDefinition = ProcessDefinition.builder()
            .withVersion(1)
            .withName("test")
            .withNodes(
                new StartDefinition(),
                new EndDefinition("end")
            ).withRelationShips(
                RelationShips.builder()
                    .withLink("start", "end")
                    .build()
            ).build();

        Mockito.when(repository.findProcessDefinition("test", 1))
            .thenReturn(Optional.of(processDefinition));

        ProcessId processId = engine.startProcess("test", 1)
            .processId(Duration.ofMinutes(1));

        assertThat(processId, notNullValue());
    }

    @Test
    public void should_throw_exception_when_continue_executing_not_exists_node() throws InterruptedException {
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

        engine.startProcess("test", 1).waitNodeStart("aService", Duration.ofMinutes(1));

        ContinueFuture future = engine.continueProcess(ProcessId.of("1"), "notExists", Variables.EMPTY);
        try {
            future.waitProcessComplete(Duration.ofMinutes(1));
            fail("Should throw exception.");
        } catch (Exception e) {
            assertThat(e.getCause(), instanceOf(ExecutionException.class));
            ExecutionException ee = (ExecutionException) e.getCause();
            assertThat(ee.getCause().getMessage(), is("No such executing node[notExists]"));
        }
    }

    @Test
    public void should_throw_exception_when_continue_executing_not_exsists_process() throws InterruptedException {
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

        engine.startProcess("test", 1).waitNodeStart("aService", Duration.ofMinutes(1));
        ContinueFuture future = engine.continueProcess(ProcessId.of("2"), "aService", Variables.EMPTY);
        try {
            future.waitProcessComplete(Duration.ofMinutes(1));
            fail("Should throw exception.");
        } catch (Exception e) {
            assertThat(e.getCause(), instanceOf(ExecutionException.class));
            ExecutionException ee = (ExecutionException) e.getCause();
            assertThat(ee.getCause().getMessage(), is("no such process instance to contine."));
        }
    }

    @Test
    public void should_throw_exception_when_process_definition_name_was_null() {
        try {
            engine.startProcess(null, 1).waitNodeComplete("test", Duration.ofMinutes(1));
            fail("Should throw exception.");
        } catch (Exception e) {
            assertThat(e.getCause().getCause().getCause().getMessage(), is("process definition name is required."));
        }
    }

    @Test
    public void should_throw_exception_when_process_definition_was_not_exists() {
        try {
            Mockito.when(repository.findProcessDefinition("test", 1))
                .thenReturn(Optional.empty());
            engine.startProcess("test", 1)
                .waitNodeStart("start", Duration.ofSeconds(1));
            fail("Should throw exception.");
        } catch (Exception e) {
        }
    }

}
