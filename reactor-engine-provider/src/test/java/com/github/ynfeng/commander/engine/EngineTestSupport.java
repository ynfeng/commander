package com.github.ynfeng.commander.engine;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.github.ynfeng.commander.definition.ProcessDefinitionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;

public class EngineTestSupport {
    protected ProcessDefinitionRepository repository;
    protected ProcessEngine engine;

    @BeforeEach
    void setup() {
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
        engine.start();

        assertThat(engine.isStarted(), is(true));
    }
}
