package com.github.ynfeng.commander.engine;

import com.github.ynfeng.commander.definition.ProcessDefinitionRepository;
import com.github.ynfeng.commander.engine.executor.SPINodeExecutors;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;

public class EngineTestSupport {
    protected ProcessDefinitionRepository repository;
    protected EngineEnvironment environment;
    protected AkkaProcessEngine engine;

    @BeforeEach
    public void setup() {
        environment = Mockito.mock(EngineEnvironment.class);
        Mockito.when(environment.getNodeExecutors()).thenReturn(
            new SPINodeExecutors());
        Mockito.when(environment.getProcessIdGenerator())
            .thenReturn(new ProcessIdGenerator() {
                @Override
                public ProcessId nextId() {
                    return ProcessId.of("1");
                }
            });
        repository = Mockito.mock(ProcessDefinitionRepository.class);
        engine = new AkkaProcessEngine(environment, repository);
        engine.startup();
    }
}
