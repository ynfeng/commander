package com.github.ynfeng.commander.core.engine;

import static org.hamcrest.CoreMatchers.instanceOf;
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
import com.github.ynfeng.commander.core.definition.NodeDefinition;
import com.github.ynfeng.commander.core.definition.ProcessDefinition;
import com.github.ynfeng.commander.core.definition.StartDefinition;
import com.github.ynfeng.commander.core.definition.TestableDefinition;
import com.github.ynfeng.commander.core.exception.ProcessEngineException;
import com.github.ynfeng.commander.core.executor.NodeExecutor;
import com.github.ynfeng.commander.core.executor.NodeExecutors;
import com.github.ynfeng.commander.core.executor.SPINodeExecutors;
import com.github.ynfeng.commander.core.executor.StartNodeExecutor;
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

        ProcessFuture processStartFuture = processEngine.startProcess(processDefinition);
        ProcessId processId = processStartFuture.processId();
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
    public void should_throw_exception_when_startup_with_out_executor_service() {
        ProcessEngine processEngine = ProcessEngine.builder()
            .processContextFactory(new ProcessContextFactory(null))
            .executorLauncher(new ExecutorLauncher(new SPINodeExecutors()))
            .build();

        ProcessEngineException exception = assertThrows(ProcessEngineException.class, () -> {
            processEngine.startUp();
        });

        assertThat(exception.getMessage(), is("java.lang.NullPointerException: Executor service not set."));
    }

    @Test
    public void should_throw_execption_when_execute_not_supported_node() {
        ProcessDefinition processDefinition = new ProcessDefinition("test", 1);
        processDefinition.firstNode(new NextableNodeDefinition("dummy") {
        });

        ProcessEngineException exception = assertThrows(ProcessEngineException.class, () -> {
            processEngine.startProcess(processDefinition).waitComplete();
        });
        assertThat(exception.getMessage(), is("Can't find any executor for dummy"));
    }

    @Test
    public void should_warp_exception_when_executor_throws_exception(){
        ProcessDefinition processDefinition = new ProcessDefinition("test", 1);
        processDefinition.firstNode(new TestableDefinition("testable"));

        ProcessEngineException exception = assertThrows(ProcessEngineException.class, () -> {
            processEngine.startProcess(processDefinition).waitComplete();
        });
        assertThat(exception.getCause(), instanceOf(NullPointerException.class));
    }

    @Override
    protected void mockExtraExecutors(NodeExecutors nodeExecutors) {
        Mockito.when(nodeExecutors.getExecutor(any(StartDefinition.class)))
            .thenReturn(new StartNodeExecutor());

        Mockito.when(nodeExecutors.getExecutor(any(TestableDefinition.class)))
            .thenReturn(new NodeExecutor() {
                @Override
                public void execute(NodeDefinition nodeDefinition) {
                    throw new NullPointerException();
                }

                @Override
                public boolean canExecute(NodeDefinition nodeDefinition) {
                    return nodeDefinition instanceof TestableDefinition;
                }
            });
    }
}
