package com.github.ynfeng.commander.core.executor;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;

import com.github.ynfeng.commander.core.ProcessEngineTestSupport;
import com.github.ynfeng.commander.core.context.ProcessStatus;
import com.github.ynfeng.commander.core.definition.EndDefinition;
import com.github.ynfeng.commander.core.definition.ProcessDefinition;
import com.github.ynfeng.commander.core.definition.ProcessDefinitionBuilder;
import com.github.ynfeng.commander.core.definition.StartDefinition;
import com.github.ynfeng.commander.core.engine.ProcessFuture;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class EndNodeExecutorTest extends ProcessEngineTestSupport {

    @Test
    public void should_execute_end_node() throws InterruptedException {
        ProcessDefinitionBuilder builder = ProcessDefinitionBuilder.create("test", 1);
        builder.createEnd("end");
        builder.createStart();
        builder.link("start", "end");
        ProcessDefinition processDefinition = builder.build();

        ProcessFuture processFuture = processEngine.startProcess(processDefinition).waitComplete();

        assertThat(processFuture.status(), is(ProcessStatus.COMPLETED));
    }

    @Override
    protected void mockExtraExecutors(NodeExecutors nodeExecutors) {
        Mockito.when(nodeExecutors.getExecutor(any(StartDefinition.class)))
            .thenReturn(new StartNodeExecutor());
        Mockito.when(nodeExecutors.getExecutor(any(EndDefinition.class)))
            .thenReturn(new EndNodeExecutor());
    }
}
