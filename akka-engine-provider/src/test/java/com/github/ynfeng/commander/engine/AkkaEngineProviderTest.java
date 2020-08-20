package com.github.ynfeng.commander.engine;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.github.ynfeng.commander.support.env.Environment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class AkkaEngineProviderTest {
    private AkkaEngineProvider provider;
    private Environment env;

    @BeforeEach
    public void setup() {
        provider = new AkkaEngineProvider();
        env = Mockito.mock(AkkaEngineEnv.class);
    }

    @Test
    public void should_get_engine() {
        ProcessEngine engine = provider.getEngine(env);

        assertThat(engine, instanceOf(AkkaProcessEngine.class));
    }

    @Test
    public void should_prepare_environment() {
        Environment env = provider.prepareEnvironment();

        assertThat(env, instanceOf(AkkaEngineEnv.class));
        assertThat(env.name(), is("akka-engine"));
    }

}
