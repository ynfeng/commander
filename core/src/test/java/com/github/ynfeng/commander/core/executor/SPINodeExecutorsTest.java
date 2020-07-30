package com.github.ynfeng.commander.core.executor;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import com.github.ynfeng.commander.core.definition.DecisionDefinition;
import com.github.ynfeng.commander.core.definition.EndDefinition;
import com.github.ynfeng.commander.core.definition.ForkDefinition;
import com.github.ynfeng.commander.core.definition.JoinDefinition;
import com.github.ynfeng.commander.core.definition.NodeDefinition;
import com.github.ynfeng.commander.core.definition.ServiceDefinition;
import com.github.ynfeng.commander.core.definition.StartDefinition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class SPINodeExecutorsTest {
    private SPINodeExecutors nodeExecutors;

    @BeforeEach
    public void setup() {
        nodeExecutors = new SPINodeExecutors();
    }

    @Test
    public void should_null_value_when_get_not_support_node_executor() {
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
        shouldGetExecuteNode(ServiceDefinition.class, ServiceNodeExecutor.class);
    }

    @Test
    public void should_get_start_node_executor() {
        shouldGetExecuteNode(StartDefinition.class, StartNodeExecutor.class);
    }

    @Test
    public void should_get_end_node_executor() {
        shouldGetExecuteNode(EndDefinition.class, EndNodeExecutor.class);
    }

    @Test
    public void should_get_fork_node_executor() {
        shouldGetExecuteNode(ForkDefinition.class, ForkNodeExecutor.class);
    }

    @Test
    public void should_get_join_node_executor() {
        shouldGetExecuteNode(JoinDefinition.class, JoinNodeExecutor.class);
    }

    @Test
    public void should_get_decision_node_executor() {
        shouldGetExecuteNode(DecisionDefinition.class, DecisionNodeExecutor.class);
    }

    private void shouldGetExecuteNode(Class<? extends NodeDefinition> nodeDefinitionClass,
                                      Class<? extends NodeExecutor> nodeExecutorClass) {
        NodeExecutor executor = nodeExecutors.getExecutor(Mockito.mock(nodeDefinitionClass));

        assertThat(executor, instanceOf(nodeExecutorClass));
    }
}
