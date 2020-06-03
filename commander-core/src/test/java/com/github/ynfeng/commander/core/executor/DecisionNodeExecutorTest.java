package com.github.ynfeng.commander.core.executor;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;

import com.github.ynfeng.commander.core.Variables;
import com.github.ynfeng.commander.core.ProcessEngineTestSupport;
import com.github.ynfeng.commander.core.context.ProcessStatus;
import com.github.ynfeng.commander.core.definition.DecisionDefinition;
import com.github.ynfeng.commander.core.definition.EndDefinition;
import com.github.ynfeng.commander.core.definition.Expression;
import com.github.ynfeng.commander.core.definition.ProcessDefinition;
import com.github.ynfeng.commander.core.definition.ProcessDefinitionBuilder;
import com.github.ynfeng.commander.core.definition.ServiceCoordinate;
import com.github.ynfeng.commander.core.definition.ServiceDefinition;
import com.github.ynfeng.commander.core.definition.StartDefinition;
import com.github.ynfeng.commander.core.engine.ProcessFuture;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class DecisionNodeExecutorTest extends ProcessEngineTestSupport {

    @Test
    public void should_execute_decision_node() throws InterruptedException {
        ProcessDefinitionBuilder builder = ProcessDefinitionBuilder.create("test", 1);
        ServiceDefinition defaultService = builder.createService("defaultService", ServiceCoordinate.of("defaultService", 1));
        ServiceDefinition aService = builder.createService("aService", ServiceCoordinate.of("aService", 1));
        builder.createService("bService", ServiceCoordinate.of("bService", 1));
        builder.createDecision("decision")
            .defaultCondition(defaultService)
            .condition(Expression.of("context.input['level'] == 1"), aService);
        builder.createStart();
        builder.createEnd("end");
        builder.link("start", "decision");
        builder.link("defaultService", "end");
        builder.link("aService", "bService");
        builder.link("bService", "end");
        ProcessDefinition processDefinition = builder.build();

        Variables variables = new Variables();
        variables.put("level", "1");
        ProcessFuture future = processEngine.startProcess(processDefinition, variables).sync();

        List<String> executedNodes = future.executedNodes();
        assertThat(executedNodes.get(2), is("aService"));
        assertThat(executedNodes.get(3), is("bService"));
        assertThat(future.status(), is(ProcessStatus.COMPLETED));
    }

    @Test
    public void should_execute_default_branch_when_no_suitable_condition() throws InterruptedException {
        ProcessDefinitionBuilder builder = ProcessDefinitionBuilder.create("test", 1);
        ServiceDefinition defaultService = builder.createService("defaultService", ServiceCoordinate.of("defaultService", 1));
        ServiceDefinition aService = builder.createService("aService", ServiceCoordinate.of("aService", 1));
        builder.createService("bService", ServiceCoordinate.of("bService", 1));
        builder.createDecision("decision")
            .defaultCondition(defaultService)
            .condition(Expression.of("context.input['level'] == 2"), aService);
        builder.createStart();
        builder.createEnd("end");
        builder.link("start", "decision");
        builder.link("defaultService", "end");
        builder.link("aService", "bService");
        builder.link("bService", "end");
        ProcessDefinition processDefinition = builder.build();

        Variables variables = new Variables();
        variables.put("level", "1");
        ProcessFuture future = processEngine.startProcess(processDefinition).sync();

        List<String> executedNodes = future.executedNodes();
        assertThat(executedNodes.get(2), is("defaultService"));
        assertThat(future.status(), is(ProcessStatus.COMPLETED));
    }

    @Override
    protected void mockExtraExecutors(NodeExecutors nodeExecutors) {
        Mockito.when(nodeExecutors.getExecutor(any(StartDefinition.class)))
            .thenReturn(new StartNodeExecutor());
        Mockito.when(nodeExecutors.getExecutor(any(EndDefinition.class)))
            .thenReturn(new EndNodeExecutor());
        Mockito.when(nodeExecutors.getExecutor(any(ServiceDefinition.class)))
            .thenReturn(new ServiceNodeExecutor());
        Mockito.when(nodeExecutors.getExecutor(any(DecisionDefinition.class)))
            .thenReturn(new DecisionNodeExecutor());
    }
}
