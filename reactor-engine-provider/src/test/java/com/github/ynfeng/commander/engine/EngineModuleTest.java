package com.github.ynfeng.commander.engine;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.Test;

class EngineModuleTest {

    @Test
    void should_get_name() {
        EngineModule engineModule = new EngineModule();

        assertThat(engineModule.name(), is("reactor-engine-module"));
    }

}
