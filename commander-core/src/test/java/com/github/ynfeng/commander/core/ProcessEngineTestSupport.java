package com.github.ynfeng.commander.core;

import com.github.ynfeng.commander.core.context.ConcurrentProcessContexts;
import com.github.ynfeng.commander.core.context.ProcessContextFactory;
import com.github.ynfeng.commander.core.context.ProcessId;
import com.github.ynfeng.commander.core.context.ProcessIdGenerator;
import com.github.ynfeng.commander.core.engine.ExecutorLauncher;
import com.github.ynfeng.commander.core.engine.ProcessEngine;
import com.github.ynfeng.commander.core.event.EventStream;
import com.github.ynfeng.commander.core.executor.NodeExecutors;
import java.util.UUID;
import java.util.concurrent.Executors;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;

public abstract class ProcessEngineTestSupport {
    protected ProcessIdGenerator processIdGenerator;
    protected ProcessEngine processEngine;
    protected NodeExecutors nodeExecutors;
    protected ConcurrentProcessContexts processContexts;

    @BeforeEach
    public void setUp() {
        nodeExecutors = Mockito.mock(NodeExecutors.class);
        mockExtraExecutors(nodeExecutors);
        mockIdGenerator();
        createProcessEngine();
    }

    private void createProcessEngine() {
        ProcessContextFactory processContextFactory = new ProcessContextFactory(processIdGenerator);
        processContexts = new ConcurrentProcessContexts();

        processEngine = ProcessEngine.builder()
            .processContextFactory(processContextFactory)
            .executorLauncher(new ExecutorLauncher(nodeExecutors))
            .executorService(Executors.newWorkStealingPool())
            .processContexts(processContexts)
            .build();

        processEngine.startUp();
    }

    private void mockIdGenerator() {
        processIdGenerator = Mockito.mock(ProcessIdGenerator.class);
        Mockito.when(processIdGenerator.nextId()).thenReturn(ProcessId.of(UUID.randomUUID().toString()));
    }

    protected abstract void mockExtraExecutors(NodeExecutors nodeExecutors);

    @AfterEach
    public void tearDown() {
        EventStream.getInstance().xxxx();
    }
}
