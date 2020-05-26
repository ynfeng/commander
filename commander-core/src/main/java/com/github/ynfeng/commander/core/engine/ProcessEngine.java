package com.github.ynfeng.commander.core.engine;


import static com.google.common.base.Preconditions.checkNotNull;

import com.github.ynfeng.commander.core.context.ProcessContext;
import com.github.ynfeng.commander.core.context.ProcessContextFactory;
import com.github.ynfeng.commander.core.context.ProcessContexts;
import com.github.ynfeng.commander.core.definition.ProcessDefinition;
import com.github.ynfeng.commander.core.event.EngineEventSubject;
import com.github.ynfeng.commander.core.event.ProcessExecuteCompletedEvent;
import com.github.ynfeng.commander.core.exception.ProcessEngineException;
import com.google.common.eventbus.Subscribe;
import java.util.concurrent.ExecutorService;
import lombok.Builder;

@Builder
public final class ProcessEngine {
    private final ProcessContexts processContexts = new ProcessContexts();
    private final ProcessContextFactory processContextFactory;
    private final ExecutorLauncher executorLauncher;
    private final ExecutorService executorService;

    public void startUp() {
        try {
            checkNotNull(processContextFactory, "ProcessContextFactory not set.");
            checkNotNull(executorLauncher, "ExecutorLauncher not set.").startUp();
            checkNotNull(executorService, "Executor service not set.");
            EngineEventSubject.getInstance().registerListener(new ProcessCompletedListener());
        } catch (Exception e) {
            throw new ProcessEngineException(e);
        }
    }

    public ProcessFuture startProcess(ProcessDefinition processDefinition) {
        ProcessContext processContext = createContext(processDefinition);
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
            ProcessContext.threadPropagate(processContext);
            EngineEventSubject.getInstance().notifyProcessStartedEvent();
        });
    }

    public int numOfRunningProcess() {
        return processContexts.size();
    }

    class ProcessCompletedListener {

        @Subscribe
        public void handleEvent(ProcessExecuteCompletedEvent event) {
            ProcessContext processContext = ProcessContext.get();
            processContexts.remove(processContext);
        }
    }
}
