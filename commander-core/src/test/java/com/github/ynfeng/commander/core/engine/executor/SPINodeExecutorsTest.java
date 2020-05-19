package com.github.ynfeng.commander.core.engine.executor;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import com.github.ynfeng.commander.core.definition.NodeDefinition;
import com.github.ynfeng.commander.core.definition.ServiceDefinition;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

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

    @Test
    public void should_get_service_node_executor() {
        NodeExecutors nodeExecutors = new SPINodeExecutors();
        NodeExecutor executor = nodeExecutors.getExecutor(Mockito.mock(ServiceDefinition.class));

        assertThat(executor, instanceOf(ServiceNodeExecutor.class));
    }

}
