package com.github.ynfeng.commander.core.engine;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.sameInstance;

import com.github.ynfeng.commander.core.context.ProcessContext;
import com.github.ynfeng.commander.core.definition.ProcessDefinition;
import com.github.ynfeng.commander.definition.ProcessDefinitionBuilder;
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
        processEngine = new ProcessEngine(processIdGenerator);
    }

    @Test
    public void should_generate_process_id_when_start_process() {
        ProcessDefinitionBuilder builder = ProcessDefinitionBuilder.create("test", 1);
        builder.createStart();
        builder.createEnd("end");
        builder.link("start", "end");
        ProcessDefinition processDefinition = builder.build();

        processEngine.startProcess(processDefinition);
        Mockito.verify(processIdGenerator).nextId();
    }

    @Test
    public void should_create_process_context_when_start_process() {
        Mockito.when(processIdGenerator.nextId()).thenReturn(ProcessId.of(UUID.randomUUID().toString()));
        ProcessDefinitionBuilder builder = ProcessDefinitionBuilder.create("test", 1);
        builder.createStart();
        builder.createEnd("end");
        builder.link("start", "end");
        ProcessDefinition processDefinition = builder.build();

        ProcessId processId = processEngine.startProcess(processDefinition);
        ProcessContext processContext = processEngine.processContext(processId);

        assertThat(processContext, notNullValue());
        assertThat(processContext.processId(), sameInstance(processId));
        assertThat(processContext.processDefinition(), sameInstance(processDefinition));
    }
}
