package com.github.ynfeng.commander.engine;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.github.ynfeng.commander.definition.ProcessDefinitionRepository;
import com.github.ynfeng.commander.support.env.Environment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class AkkaEngineProviderTest {
    private ReactorEngineProvider provider;
    private EngineEnvironment env;

    @BeforeEach
    public void setup() {
        provider = new ReactorEngineProvider();
        env = Mockito.mock(EngineEnvironment.class);
    }

    @Test
    public void should_get_engine() {
        ProcessEngine engine = provider.getEngine(env, Mockito.mock(ProcessDefinitionRepository.class));

        assertThat(engine, instanceOf(ReactorProcessEngine.class));
    }

    @Test
    public void should_prepare_environment() {
        Environment env = provider.prepareEnvironment();

        assertThat(env, instanceOf(ReactorEngineEnv.class));
        assertThat(env.name(), is("akka-engine"));
    }

}
