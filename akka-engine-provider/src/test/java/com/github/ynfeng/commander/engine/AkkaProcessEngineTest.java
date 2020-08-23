package com.github.ynfeng.commander.engine;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

import com.github.ynfeng.commander.definition.EndDefinition;
import com.github.ynfeng.commander.definition.ProcessDefinition;
import com.github.ynfeng.commander.definition.ProcessDefinitionRepository;
import com.github.ynfeng.commander.definition.RelationShips;
import com.github.ynfeng.commander.definition.StartDefinition;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class AkkaProcessEngineTest {

    private ProcessDefinitionRepository repository;
    private AkkaProcessEngine engine;

    @BeforeEach
    public void setup() {
        repository = Mockito.mock(ProcessDefinitionRepository.class);
        engine = new AkkaProcessEngine(new UUIDProcessIdGenerator(), repository);
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

        engine.startup();
        engine.startProcess("test", 1).join();
        engine.shutdown();
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
