package com.github.ynfeng.commander.core.executor;

import static org.junit.jupiter.api.Assertions.assertThrows;

import com.github.ynfeng.commander.core.definition.NodeDefinition;
import com.github.ynfeng.commander.core.engine.ProcessEngineException;
import org.junit.jupiter.api.Test;

class NodeExecutorsTest {

    @Test
    public void should_throw_exception_when_get_not_support_node_executor() {
        NodeExecutors nodeExecutors = new NodeExecutors();
        nodeExecutors.load();

        assertThrows(ProcessEngineException.class, () -> {
            nodeExecutors.getExecutor(new NodeDefinition() {
                @Override
                public String refName() {
                    return "";
                }
            });
        });
    }

}
