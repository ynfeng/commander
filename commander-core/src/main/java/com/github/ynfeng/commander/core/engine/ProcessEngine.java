package com.github.ynfeng.commander.core.engine;


import com.github.ynfeng.commander.core.context.ProcessContext;
import com.github.ynfeng.commander.core.context.ProcessContextFactory;
import com.github.ynfeng.commander.core.context.ProcessContexts;
import com.github.ynfeng.commander.core.context.ProcessId;
import com.github.ynfeng.commander.core.definition.ProcessDefinition;
import com.github.ynfeng.commander.core.event.ProcessStartEvent;

public final class ProcessEngine {
    private final ProcessContexts processContexts = new ProcessContexts();
    private final ProcessContextFactory processContextFactory;

    public ProcessEngine(ProcessContextFactory processContextFactory) {
        this.processContextFactory = processContextFactory;
    }

    public void startUp() {
        new ExecutorLauncher().startUp();
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
