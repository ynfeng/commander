package com.github.ynfeng.commander.core.context;

import com.github.ynfeng.commander.core.definition.ProcessDefinition;

public class ProcessContextFactory {
    private final ProcessIdGenerator processIdGenerator;

    public ProcessContextFactory(ProcessIdGenerator processIdGenerator) {
        this.processIdGenerator = processIdGenerator;
    }

    public ProcessContext createContext(ProcessDefinition processDefinition) {
        ProcessId processId = processIdGenerator.nextId();
        return ProcessContext.create(processId, processDefinition);
    }
}