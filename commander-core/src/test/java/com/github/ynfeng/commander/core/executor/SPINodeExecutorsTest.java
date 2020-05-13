package com.github.ynfeng.commander.core.executor;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import com.github.ynfeng.commander.core.definition.NodeDefinition;
import org.junit.jupiter.api.Test;

class SPINodeExecutorsTest {

    @Test
    public void should_null_value_when_get_not_support_node_executor() {
        NodeExecutors nodeExecutors = new SPINodeExecutors();

        NodeExecutor executor = nodeExecutors.getExecutor(new NodeDefinition() {
            @Override
            public String refName() {
                return "";
            }
        });

        assertThat(executor, nullValue());
    }

}
