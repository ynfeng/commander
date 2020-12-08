package com.github.ynfeng.commander.engine;

import com.github.ynfeng.commander.definition.ProcessDefinitionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;

public class EngineTestSupport {
    protected ProcessDefinitionRepository repository;
    protected ProcessEngine engine;

    @BeforeEach
    public void setup() {
        EngineModule engineModule = new EngineModule();
        engineModule.init();
        repository = Mockito.mock(ProcessDefinitionRepository.class);
        engineModule.registerComponent(ProcessDefinitionRepository.class, repository);
        engineModule.registerComponent(ProcessIdGenerator.class, new ProcessIdGenerator() {
            @Override
            public ProcessId nextId() {
                return ProcessId.of("1");
            }
        });
        engine = engineModule.getEngine();
        engine.startup();
    }
}
