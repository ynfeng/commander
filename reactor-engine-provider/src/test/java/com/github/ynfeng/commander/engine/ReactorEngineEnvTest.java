package com.github.ynfeng.commander.engine;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.github.ynfeng.commander.engine.executor.SPINodeExecutors;
import org.junit.jupiter.api.Test;

class ReactorEngineEnvTest {

    @Test
    public void should_create_akka_engine_environment() {
        ReactorEngineEnv engineEnv = new ReactorEngineEnv(null);

        assertThat(engineEnv.name(), is("akka-engine"));
        assertThat(engineEnv.getProcessIdGenerator(), instanceOf(UUIDProcessIdGenerator.class));
        assertThat(engineEnv.getNodeExecutors(), instanceOf(SPINodeExecutors.class));
    }

}
