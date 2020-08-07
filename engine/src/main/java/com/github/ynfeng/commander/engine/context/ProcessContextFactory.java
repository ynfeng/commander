package com.github.ynfeng.commander.engine.context;

import com.github.ynfeng.commander.definition.ProcessDefinition;

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
