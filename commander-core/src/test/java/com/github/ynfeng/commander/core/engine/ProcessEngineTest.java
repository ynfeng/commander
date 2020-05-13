package com.github.ynfeng.commander.core.engine;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.github.ynfeng.commander.core.context.ProcessContext;
import com.github.ynfeng.commander.core.context.ProcessContextFactory;
import com.github.ynfeng.commander.core.context.ProcessId;
import com.github.ynfeng.commander.core.context.ProcessIdGenerator;
import com.github.ynfeng.commander.core.definition.FakeNodeDefinition;
import com.github.ynfeng.commander.core.definition.ProcessDefinition;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class ProcessEngineTest {
    private ProcessIdGenerator processIdGenerator;
    private ProcessEngine processEngine;

    @BeforeEach
    public void setUp() {
        processIdGenerator = Mockito.mock(ProcessIdGenerator.class);
        Mockito.when(processIdGenerator.nextId()).thenReturn(ProcessId.of(UUID.randomUUID().toString()));
        ProcessContextFactory processContextFactory = new ProcessContextFactory(processIdGenerator);

        processEngine = ProcessEngine.builder()
            .processContextFactory(processContextFactory)
            .executorLauncher(new ExecutorLauncher())
            .build();

        processEngine.startUp();
    }

    @Test
    public void should_generate_process_id_when_start_process() {
        ProcessDefinition processDefinition = new ProcessDefinition("test", 1);

        processEngine.startProcess(processDefinition);
        Mockito.verify(processIdGenerator).nextId();
    }

    @Test
    public void should_create_process_context_when_start_process() {
        ProcessDefinition processDefinition = new ProcessDefinition("test", 1);
        processDefinition.firstNode(new FakeNodeDefinition("fake"));
        ProcessId processId = processEngine.startProcess(processDefinition);
        ProcessContext processContext = processEngine.processContext(processId);

        assertThat(processContext, notNullValue());
        assertThat(processContext.processId(), sameInstance(processId));
        assertThat(processContext.processDefinition(), sameInstance(processDefinition));
    }

    @Test
    public void should_throw_exception_when_startup_with_out_process_context_factory() {
        ProcessEngine processEngine = ProcessEngine.builder().build();
        ProcessEngineException exception = assertThrows(ProcessEngineException.class, () -> {
            processEngine.startUp();
        });

        assertThat(exception.getMessage(), is("java.lang.NullPointerException: ProcessContextFactory not set."));
    }

    @Test
    public void should_throw_exception_when_startup_with_out_executor_launcher() {
        ProcessEngine processEngine = ProcessEngine.builder()
            .processContextFactory(new ProcessContextFactory(null))
            .build();
        ProcessEngineException exception = assertThrows(ProcessEngineException.class, () -> {
            processEngine.startUp();
        });

        assertThat(exception.getMessage(), is("java.lang.NullPointerException: ExecutorLauncher not set."));
    }
}
