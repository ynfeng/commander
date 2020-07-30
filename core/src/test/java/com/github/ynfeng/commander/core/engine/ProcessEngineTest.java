package com.github.ynfeng.commander.core.engine;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

import com.github.ynfeng.commander.core.ProcessEngineTestSupport;
import com.github.ynfeng.commander.core.Variables;
import com.github.ynfeng.commander.core.context.ProcessContext;
import com.github.ynfeng.commander.core.context.ProcessContextFactory;
import com.github.ynfeng.commander.core.context.ProcessStatus;
import com.github.ynfeng.commander.core.definition.EndDefinition;
import com.github.ynfeng.commander.core.definition.NextableNodeDefinition;
import com.github.ynfeng.commander.core.definition.NodeDefinition;
import com.github.ynfeng.commander.core.definition.ProcessDefinition;
import com.github.ynfeng.commander.core.definition.RelationShips;
import com.github.ynfeng.commander.core.definition.StartDefinition;
import com.github.ynfeng.commander.core.definition.TestableDefinition;
import com.github.ynfeng.commander.core.exception.ProcessEngineException;
import com.github.ynfeng.commander.core.executor.EndNodeExecutor;
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
    public void should_clear_porcess_context_when_process_completed() throws InterruptedException {
        ProcessDefinition processDefinition = ProcessDefinition.builder()
            .withVersion(1)
            .withName("fooTest")
            .withNodes(
                new StartDefinition(),
                new EndDefinition("end")
            ).withRelationShips(
                RelationShips.builder()
                    .withLink("start", "end")
                    .build()
            ).build();

        processEngine.startProcess(processDefinition).sync();
        assertThat(processEngine.numOfRunningProcess(), is(0));
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

        assertThat(exception.getMessage(), is("java.lang.NullPointerException: ExecutorService not set."));
    }

    @Test
    public void should_throw_execption_when_execute_not_supported_node() {
        ProcessDefinition processDefinition = new ProcessDefinition("test", 1);
        processDefinition.firstNode(new NextableNodeDefinition("dummy") {
        });

        ProcessEngineException exception = assertThrows(ProcessEngineException.class, () -> {
            processEngine.startProcess(processDefinition).sync();
        });
        assertThat(exception.getMessage(), is("Can't find any executor for dummy"));
    }

    @Test
    public void should_faild_status_when_execute_exeception() throws InterruptedException {
        ProcessDefinition processDefinition = new ProcessDefinition("test", 1);
        processDefinition.firstNode(new NextableNodeDefinition("dummy") {
        });

        ProcessFuture processFuture = processEngine.startProcess(processDefinition);
        processFuture.syncNotThrowException();

        assertThat(processFuture.status(), is(ProcessStatus.FAILED));
    }

    @Test
    public void should_throw_exception_when_executor_throw_exception() {
        ProcessDefinition processDefinition = new ProcessDefinition("test", 1);
        processDefinition.firstNode(new TestableDefinition("testable"));

        ProcessEngineException exception = assertThrows(ProcessEngineException.class, () -> {
            processEngine.startProcess(processDefinition).sync();
        });
        assertThat(exception.getCause(), instanceOf(NullPointerException.class));
    }

    @Test
    public void should_start_process_with_variables() throws InterruptedException {
        ProcessDefinition processDefinition = ProcessDefinition.builder()
            .withName("fooTest")
            .withVersion(1)
            .withNodes(
                new StartDefinition(),
                new EndDefinition("end")
            )
            .withRelationShips(
                RelationShips.builder()
                    .withLink("start", "end")
                    .build()
            )
            .build();

        Variables variables = new Variables();
        variables.put("foo", "bar");

        ProcessFuture future = processEngine.startProcess(processDefinition, variables).sync();

        assertThat(future.contextVariables().get("foo"), is("bar"));
    }

    @Override
    protected void mockExtraExecutors(NodeExecutors nodeExecutors) {
        Mockito.when(nodeExecutors.getExecutor(any(StartDefinition.class)))
            .thenReturn(new StartNodeExecutor());

        Mockito.when(nodeExecutors.getExecutor(any(EndDefinition.class)))
            .thenReturn(new EndNodeExecutor());

        Mockito.when(nodeExecutors.getExecutor(any(TestableDefinition.class)))
            .thenReturn(new NodeExecutor() {
                @Override
                public void execute(ProcessContext context, NodeDefinition nodeDefinition) {
                    throw new NullPointerException();
                }

                @Override
                public boolean canExecute(NodeDefinition nodeDefinition) {
                    return nodeDefinition instanceof TestableDefinition;
                }
            });
    }
}
