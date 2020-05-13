package com.github.ynfeng.commander.core.engine;


import static com.google.common.base.Preconditions.checkNotNull;

import com.github.ynfeng.commander.core.context.ProcessContext;
import com.github.ynfeng.commander.core.context.ProcessContextFactory;
import com.github.ynfeng.commander.core.context.ProcessContexts;
import com.github.ynfeng.commander.core.context.ProcessId;
import com.github.ynfeng.commander.core.definition.ProcessDefinition;
import com.github.ynfeng.commander.core.event.ProcessStartEvent;
import lombok.Builder;

@Builder
public final class ProcessEngine {
    private final ProcessContexts processContexts = new ProcessContexts();
    private ProcessContextFactory processContextFactory;
    private ExecutorLauncher executorLauncher;

    public void startUp() {
        try {
            checkNotNull(processContextFactory, "ProcessContextFactory not set.");
            checkNotNull(executorLauncher, "ExecutorLauncher not set.").startUp();
        } catch (Exception e) {
            throw new ProcessEngineException(e);
        }
    }

    public ProcessId startProcess(ProcessDefinition processDefinition) {
        ProcessContext processContext = processContextFactory.createContext(processDefinition);
        processContexts.add(processContext);
        EngineContext.publishEvent(ProcessStartEvent.create(processContext));
        return processContext.processId();
    }

    public ProcessContext processContext(ProcessId processId) {
        return processContexts.get(processId);
    }
}
