package com.github.ynfeng.commander.core.engine;


import static com.google.common.base.Preconditions.checkNotNull;

import com.github.ynfeng.commander.core.Variables;
import com.github.ynfeng.commander.core.context.ProcessContext;
import com.github.ynfeng.commander.core.context.ProcessContextFactory;
import com.github.ynfeng.commander.core.context.ProcessContexts;
import com.github.ynfeng.commander.core.context.event.NodeExecuteEvent;
import com.github.ynfeng.commander.core.context.event.ProcessContextClearedEvent;
import com.github.ynfeng.commander.core.context.event.ProcessExecuteCompletedEvent;
import com.github.ynfeng.commander.core.context.event.ProcessExecuteFailedEvent;
import com.github.ynfeng.commander.core.context.event.ProcessStartedEvent;
import com.github.ynfeng.commander.core.definition.ProcessDefinition;
import com.github.ynfeng.commander.core.event.EventStream;
import com.github.ynfeng.commander.core.exception.ProcessEngineException;
import com.google.common.eventbus.Subscribe;
import java.util.concurrent.ExecutorService;
import lombok.Builder;

@Builder
public final class ProcessEngine {
    private final ProcessContexts processContexts;
    private final ProcessContextFactory processContextFactory;
    private final ExecutorLauncher executorLauncher;
    private final ExecutorService executorService;

    public void startUp() {
        try {
            checkRequiredCompments();
            listenProcessCompleted();
        } catch (Exception e) {
            throw new ProcessEngineException(e);
        }
    }

    private void listenProcessCompleted() {
        EventStream.getInstance().subcribe(new ProcessCompletedListener());
    }

    private void checkRequiredCompments() {
        checkNotNull(processContextFactory, "ProcessContextFactory not set.");
        checkNotNull(executorLauncher, "ExecutorLauncher not set.").startUp();
        checkNotNull(executorService, "ExecutorService not set.");
        checkNotNull(processContexts, "ProcessContexts not set.");
    }

    public ProcessFuture startProcess(ProcessDefinition processDefinition) {
        return startProcess(processDefinition, new Variables());
    }

    public ProcessFuture startProcess(ProcessDefinition processDefinition, Variables variables) {
        ProcessContext processContext = createContext(processDefinition);
        processContext.input(variables);
        publishProcessStartEvent(processContext);
        return ProcessFuture.create(processContext);
    }

    private ProcessContext createContext(ProcessDefinition processDefinition) {
        ProcessContext processContext = processContextFactory.createContext(processDefinition);
        processContexts.add(processContext);
        return processContext;
    }

    private void publishProcessStartEvent(ProcessContext processContext) {
        executorService.execute(() -> {
            EventStream.getInstance().publish(new ProcessStartedEvent(processContext));
        });
    }

    public int numOfRunningProcess() {
        return processContexts.size();
    }

    class ProcessCompletedListener {

        @Subscribe
        public void handleEvent(NodeExecuteEvent event) {
            if (isRunFinish(event)) {
                processContexts.remove(event.processContext());
                EventStream.getInstance().publish(new ProcessContextClearedEvent(event.processContext()));
            }
        }

        private boolean isRunFinish(NodeExecuteEvent event) {
            return event instanceof ProcessExecuteCompletedEvent || event instanceof ProcessExecuteFailedEvent;
        }
    }
}
