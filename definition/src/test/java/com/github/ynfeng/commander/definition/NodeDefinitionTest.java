package com.github.ynfeng.commander.definition;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.Test;

class NodeDefinitionTest {

    @Test
    public void ref_name_should_null_given_null_node() {
        assertThat(NodeDefinition.NULL.refName(), nullValue());
    }

}
