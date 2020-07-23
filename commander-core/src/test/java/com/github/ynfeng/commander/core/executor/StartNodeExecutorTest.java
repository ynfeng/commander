package com.github.ynfeng.commander.core.executor;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;

import com.github.ynfeng.commander.core.ProcessEngineTestSupport;
import com.github.ynfeng.commander.core.context.ProcessStatus;
import com.github.ynfeng.commander.core.definition.ProcessDefinition;
import com.github.ynfeng.commander.core.definition.ProcessDefinitionBuilder;
import com.github.ynfeng.commander.core.definition.StartDefinition;
import com.github.ynfeng.commander.core.engine.ProcessFuture;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class StartNodeExecutorTest extends ProcessEngineTestSupport {

    @Test
    public void should_execute_start_node() throws InterruptedException {
        ProcessDefinitionBuilder builder = ProcessDefinition.builder().withName("test").withVersion(1);
        builder.createStart();
        ProcessDefinition processDefinition = builder.build();

        ProcessFuture processFuture = processEngine.startProcess(processDefinition).sync(500, TimeUnit.MILLISECONDS);

        assertThat(processFuture.status(), is(ProcessStatus.RUNNING));
    }

    @Override
    protected void mockExtraExecutors(NodeExecutors nodeExecutors) {
        Mockito.when(nodeExecutors.getExecutor(any(StartDefinition.class)))
            .thenReturn(new StartNodeExecutor());
    }
}
