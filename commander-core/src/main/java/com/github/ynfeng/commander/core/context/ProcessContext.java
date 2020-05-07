package com.github.ynfeng.commander.core.context;


import com.github.ynfeng.commander.core.definition.ProcessDefinition;
import com.github.ynfeng.commander.core.engine.ProcessId;

public final class ProcessContext {
    private final ProcessId processId;
    private final ProcessDefinition processDefinition;

    public ProcessContext(ProcessId processId, ProcessDefinition processDefinition) {
        this.processId = processId;
        this.processDefinition = processDefinition;
    }

    public ProcessId processId() {
        return processId;
    }

    public ProcessDefinition processDefinition() {
        return processDefinition;
    }
}
