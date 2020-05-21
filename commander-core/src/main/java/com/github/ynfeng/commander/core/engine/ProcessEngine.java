package com.github.ynfeng.commander.core.engine;


import static com.google.common.base.Preconditions.checkNotNull;

import com.github.ynfeng.commander.core.context.ProcessContext;
import com.github.ynfeng.commander.core.context.ProcessContextFactory;
import com.github.ynfeng.commander.core.context.ProcessContexts;
import com.github.ynfeng.commander.core.context.ProcessId;
import com.github.ynfeng.commander.core.context.ProcessStartFuture;
import com.github.ynfeng.commander.core.definition.ProcessDefinition;
import com.github.ynfeng.commander.core.event.ProcessStartEvent;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
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
        } catch (Exception e) {
            throw new ProcessEngineException(e);
        }
    }

    public ProcessStartFuture startProcess(ProcessDefinition processDefinition) {
        ProcessContext processContext = createContext(processDefinition);
        final Future<?> future = publishProcessStartEvent(processContext);
        return ProcessStartFuture.create(processContext.processId(), future);
    }

    private ProcessContext createContext(ProcessDefinition processDefinition) {
        ProcessContext processContext = processContextFactory.createContext(processDefinition);
        processContexts.add(processContext);
        return processContext;
    }

    private Future<?> publishProcessStartEvent(ProcessContext processContext) {
        return executorService.submit(() -> {
            EngineContext.publishEvent(ProcessStartEvent.create(processContext));
        });
    }

    public ProcessContext processContext(ProcessId processId) {
        return processContexts.get(processId);
    }
}
