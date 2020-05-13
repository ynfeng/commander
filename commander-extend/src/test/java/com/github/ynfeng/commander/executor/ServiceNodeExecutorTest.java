package com.github.ynfeng.commander.executor;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.github.ynfeng.commander.core.context.ProcessContext;
import com.github.ynfeng.commander.core.context.ProcessId;
import com.github.ynfeng.commander.core.context.ProcessStatus;
import com.github.ynfeng.commander.core.definition.ProcessDefinition;
import com.github.ynfeng.commander.definition.ProcessDefinitionBuilder;
import com.github.ynfeng.commander.definition.ServiceCoordinate;
import org.junit.jupiter.api.Test;

public class ServiceNodeExecutorTest extends ProcessEngineTestSupport {

    @Test
    public void should_execute_service_node() {
        ProcessDefinitionBuilder builder = ProcessDefinitionBuilder.create("test", 1);
        builder.createStart();
        builder.createService("aService", ServiceCoordinate.of("aService", 1));
        builder.createEnd("end");
        builder.link("start", "aService");
        builder.link("aService", "end");

        ProcessDefinition processDefinition = builder.build();

        ProcessId processId = processEngine.startProcess(processDefinition);
        ProcessContext processContext = processEngine.processContext(processId);

        assertThat(processContext.executedNodes().get(1), is("aService"));
        assertThat(processContext.status(), is(ProcessStatus.COMPLETED));
    }
}
