package com.github.ynfeng.commander.core.engine.executor;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;

import com.github.ynfeng.commander.core.ProcessEngineTestSupport;
import com.github.ynfeng.commander.core.context.ProcessContext;
import com.github.ynfeng.commander.core.context.ProcessId;
import com.github.ynfeng.commander.core.context.ProcessStatus;
import com.github.ynfeng.commander.core.definition.EndDefinition;
import com.github.ynfeng.commander.core.definition.ProcessDefinition;
import com.github.ynfeng.commander.core.definition.ProcessDefinitionBuilder;
import com.github.ynfeng.commander.core.definition.ServiceCoordinate;
import com.github.ynfeng.commander.core.definition.ServiceDefinition;
import com.github.ynfeng.commander.core.definition.StartDefinition;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class ServiceNodeExecutorTest extends ProcessEngineTestSupport {

    @Test
    public void should_execute_service_node() throws InterruptedException {
        ProcessDefinitionBuilder builder = ProcessDefinitionBuilder.create("test", 1);
        builder.createStart();
        builder.createService("aService", ServiceCoordinate.of("aService", 1));
        builder.createEnd("end");
        builder.link("start", "aService");
        builder.link("aService", "end");

        ProcessDefinition processDefinition = builder.build();

        ProcessId processId = processEngine.startProcess(processDefinition).waitComplete().processId();
        ProcessContext processContext = processEngine.processContext(processId);

        assertThat(processContext.status(), is(ProcessStatus.COMPLETED));
    }

    @Override
    protected void mockExtraExecutors(NodeExecutors nodeExecutors) {
        Mockito.when(nodeExecutors.getExecutor(any(StartDefinition.class)))
            .thenReturn(new StartNodeExecutor());
        Mockito.when(nodeExecutors.getExecutor(any(EndDefinition.class)))
            .thenReturn(new EndNodeExecutor());
        Mockito.when(nodeExecutors.getExecutor(any(ServiceDefinition.class)))
            .thenReturn(new ServiceNodeExecutor());
    }
}
