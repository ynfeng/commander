package com.github.ynfeng.commander.core.context;

import com.github.ynfeng.commander.core.definition.ProcessDefinition;
import com.github.ynfeng.commander.core.engine.ProcessId;
import com.github.ynfeng.commander.core.engine.ProcessIdGenerator;

public class ProcessContextFactory {
    private final ProcessIdGenerator processIdGenerator;

    public ProcessContextFactory(ProcessIdGenerator processIdGenerator) {
        this.processIdGenerator = processIdGenerator;
    }

    public ProcessContext createContext(ProcessDefinition processDefinition) {
        ProcessId processId = processIdGenerator.nextId();
        return new ProcessContext(processId, processDefinition);
    }
}
