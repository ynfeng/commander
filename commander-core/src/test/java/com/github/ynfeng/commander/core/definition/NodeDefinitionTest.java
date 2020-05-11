package com.github.ynfeng.commander.core.definition;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.Test;

class NodeDefinitionTest {

    @Test
    public void should_get_ref_name() {
        assertThat(NodeDefinition.NULL.refName(), nullValue());
    }

}
