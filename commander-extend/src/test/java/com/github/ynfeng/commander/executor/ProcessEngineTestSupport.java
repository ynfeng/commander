package com.github.ynfeng.commander.executor;

import com.github.ynfeng.commander.core.context.ProcessContextFactory;
import com.github.ynfeng.commander.core.context.ProcessId;
import com.github.ynfeng.commander.core.context.ProcessIdGenerator;
import com.github.ynfeng.commander.core.engine.ProcessEngine;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;

public class ProcessEngineTestSupport {
    protected ProcessIdGenerator processIdGenerator;
    protected ProcessEngine processEngine;

    @BeforeEach
    public void setUp() {
        processIdGenerator = Mockito.mock(ProcessIdGenerator.class);
        Mockito.when(processIdGenerator.nextId()).thenReturn(ProcessId.of(UUID.randomUUID().toString()));
        processEngine = new ProcessEngine(new ProcessContextFactory(processIdGenerator));
        processEngine.startUp();
    }
}
