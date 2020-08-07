package com.github.ynfeng.commander.engine.executor;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;

import com.github.ynfeng.commander.engine.ProcessEngineTestSupport;
import com.github.ynfeng.commander.engine.context.ProcessStatus;
import com.github.ynfeng.commander.engine.engine.ProcessFuture;
import com.github.ynfeng.commander.definition.ProcessDefinition;
import com.github.ynfeng.commander.definition.StartDefinition;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class StartNodeExecutorTest extends ProcessEngineTestSupport {

    @Test
    public void should_execute_start_node() throws InterruptedException {
        ProcessDefinition processDefinition = ProcessDefinition.builder()
            .withName("test")
            .withVersion(1)
            .withNodes(new StartDefinition())
            .build();

        ProcessFuture processFuture = processEngine.startProcess(processDefinition).sync(500, TimeUnit.MILLISECONDS);

        assertThat(processFuture.status(), is(ProcessStatus.RUNNING));
    }

    @Override
    protected void mockExtraExecutors(NodeExecutors nodeExecutors) {
        Mockito.when(nodeExecutors.getExecutor(any(StartDefinition.class)))
            .thenReturn(new StartNodeExecutor());
    }
}
