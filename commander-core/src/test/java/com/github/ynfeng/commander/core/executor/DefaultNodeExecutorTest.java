package com.github.ynfeng.commander.core.executor;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.github.ynfeng.commander.core.definition.NodeDefinition;
import org.junit.jupiter.api.Test;

class DefaultNodeExecutorTest {

    @Test
    public void should_execute_null() {
        DefaultNodeExecutor defaultNodeExecutor = new DefaultNodeExecutor();
        boolean canExecute = defaultNodeExecutor.canExecute(null);
        assertThat(canExecute, is(true));
    }

    @Test
    public void should_execute_null_node() {
        DefaultNodeExecutor defaultNodeExecutor = new DefaultNodeExecutor();
        boolean canExecute = defaultNodeExecutor.canExecute(NodeDefinition.NULL);
        assertThat(canExecute, is(true));
    }


}
