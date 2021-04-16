package com.github.ynfeng.commander.engine;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.Test;

class EngineConfigTest {

    @Test
    void should_get_component_config() {
        EngineConfig engineConfig = new EngineConfig();

        assertThat(engineConfig.getComponentConfig(""), nullValue());
    }

}
