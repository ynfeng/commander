package com.github.ynfeng.commander.core.engine;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

import com.github.ynfeng.commander.core.ProcessEngineTestSupport;
import com.github.ynfeng.commander.core.context.ProcessContext;
import com.github.ynfeng.commander.core.context.ProcessContextFactory;
import com.github.ynfeng.commander.core.context.ProcessId;
import com.github.ynfeng.commander.core.definition.NextableNodeDefinition;
import com.github.ynfeng.commander.core.definition.ProcessDefinition;
import com.github.ynfeng.commander.core.definition.StartDefinition;
import com.github.ynfeng.commander.core.engine.executor.NodeExecutors;
import com.github.ynfeng.commander.core.engine.executor.StartNodeExecutor;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class ProcessEngineTest extends ProcessEngineTestSupport {

    @Test
    public void should_generate_process_id_when_start_process() {
        ProcessDefinition processDefinition = new ProcessDefinition("test", 1);

        processEngine.startProcess(processDefinition);
        Mockito.verify(processIdGenerator).nextId();
    }

    @Test
    public void should_create_process_context_when_start_process() {
        ProcessDefinition processDefinition = new ProcessDefinition("test", 1);
        processDefinition.firstNode(new StartDefinition());

        ProcessId processId = processEngine.startProcess(processDefinition);
        ProcessContext processContext = processEngine.processContext(processId);

        assertThat(processContext, notNullValue());
        assertThat(processContext.processId(), sameInstance(processId));
        assertThat(processContext.processDefinition(), sameInstance(processDefinition));
    }

    @Test
    public void should_throw_exception_when_startup_with_out_process_context_factory() {
        ProcessEngine processEngine = ProcessEngine.builder().build();
        ProcessEngineException exception = assertThrows(ProcessEngineException.class, () -> {
            processEngine.startUp();
        });

        assertThat(exception.getMessage(), is("java.lang.NullPointerException: ProcessContextFactory not set."));
    }

    @Test
    public void should_throw_exception_when_startup_with_out_executor_launcher() {
        ProcessEngine processEngine = ProcessEngine.builder()
            .processContextFactory(new ProcessContextFactory(null))
            .build();
        ProcessEngineException exception = assertThrows(ProcessEngineException.class, () -> {
            processEngine.startUp();
        });

        assertThat(exception.getMessage(), is("java.lang.NullPointerException: ExecutorLauncher not set."));
    }

    @Test
    public void should_throw_execption_when_execute_not_supported_node() {
        ProcessDefinition processDefinition = new ProcessDefinition("test", 1);
        processDefinition.firstNode(new NextableNodeDefinition("dummy") {
        });

        ProcessEngineException exception = assertThrows(ProcessEngineException.class, () -> {
            processEngine.startProcess(processDefinition);
        });
        assertThat(exception.getMessage(), is("Can't find any executor for dummy"));
    }

    @Override
    protected void mockExtraExecutors(NodeExecutors nodeExecutors) {
        Mockito.when(nodeExecutors.getExecutor(any(StartDefinition.class)))
            .thenReturn(new StartNodeExecutor());
    }
}
