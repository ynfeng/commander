package com.github.ynfeng.commander.engine;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

import com.github.ynfeng.commander.definition.EndDefinition;
import com.github.ynfeng.commander.definition.NodeDefinition;
import com.github.ynfeng.commander.definition.ProcessDefinition;
import com.github.ynfeng.commander.definition.RelationShips;
import com.github.ynfeng.commander.definition.ServiceCoordinate;
import com.github.ynfeng.commander.definition.ServiceDefinition;
import com.github.ynfeng.commander.definition.StartDefinition;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletionException;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.AfterEach;
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

        ProcessInstanceInfo info = engine.startProcess("test", 1).join();

        List<NodeDefinition> nodeDefinitions = info.executedNodes();
        assertThat(nodeDefinitions.get(0).refName(), is("start"));
        assertThat(nodeDefinitions.get(1).refName(), is("end"));
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

        try {
            engine.startProcess("test", 1).get(1, TimeUnit.SECONDS);
        } catch (Exception e) {
        }

        ProcessFuture processFuture = engine.continueProcess(ProcessId.of("1"), "notExists", Variables.EMPTY);
        try {
            processFuture.join();
            fail("Should throw exception.");
        } catch (CompletionException e) {
            assertThat(e.getCause(), instanceOf(ProcessEngineException.class));
            ProcessEngineException pe = (ProcessEngineException) e.getCause();
            assertThat(pe.getMessage(), is("No such executing node[notExists]"));
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

        try {
            engine.startProcess("test", 1).get(1, TimeUnit.SECONDS);
        } catch (Exception e) {
        }

        ProcessFuture processFuture = engine.continueProcess(ProcessId.of("2"), "aService", Variables.EMPTY);
        try {
            processFuture.join();
            fail("Should throw exception.");
        } catch (CompletionException e) {
            assertThat(e.getCause(), instanceOf(ProcessEngineException.class));
            ProcessEngineException pe = (ProcessEngineException) e.getCause();
            assertThat(pe.getMessage(), is("No such process instance [2]"));
        }
    }

    @Test
    public void should_throw_exception_when_process_definition_name_was_null() {
        try {
            engine.startProcess(null, 1);
            fail("Should throw exception.");
        } catch (NullPointerException e) {
            assertThat(e.getMessage(), is("process definition name is required."));
        }
    }

    @Test
    public void should_throw_exception_when_process_definition_was_not_exists() {
        try {
            Mockito.when(repository.findProcessDefinition("test", 1))
                .thenReturn(Optional.empty());
            engine.startProcess("test", 1);
            fail("Should throw exception.");
        } catch (ProcessEngineException e) {
            assertThat(e.getMessage(), is("process definition was not exists."));
        }
    }

}
