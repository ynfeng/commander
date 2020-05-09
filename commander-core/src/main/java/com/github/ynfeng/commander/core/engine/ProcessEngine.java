package com.github.ynfeng.commander.core.engine;


import com.github.ynfeng.commander.core.context.ProcessContext;
import com.github.ynfeng.commander.core.context.ProcessContextFactory;
import com.github.ynfeng.commander.core.context.ProcessContexts;
import com.github.ynfeng.commander.core.definition.ProcessDefinition;
import com.github.ynfeng.commander.core.event.ProcessContextCreatedEvent;
import com.github.ynfeng.commander.core.event.ProcessEngineEventBus;
import com.github.ynfeng.commander.core.listener.ProcessContextCreatedEventListener;

public final class ProcessEngine {
    private final ProcessContexts processContexts = new ProcessContexts();
    private final ProcessContextFactory processContextFactory;
    private final ProcessEngineEventBus eventBus = new ProcessEngineEventBus();

    public ProcessEngine(ProcessContextFactory processContextFactory) {
        this.processContextFactory = processContextFactory;
    }

    public void startUp() {
        eventBus.registerListener(new ProcessContextCreatedEventListener());
    }

    public ProcessId startProcess(ProcessDefinition processDefinition) {
        ProcessContext processContext = processContextFactory.createContext(processDefinition);
        processContexts.add(processContext);
        eventBus.publishEvent(ProcessContextCreatedEvent.create(processContext));
        return processContext.processId();
    }

    public ProcessContext processContext(ProcessId processId) {
        return processContexts.get(processId);
    }
}
