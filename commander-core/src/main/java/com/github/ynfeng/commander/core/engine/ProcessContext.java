package com.github.ynfeng.commander.core.engine;

import com.github.ynfeng.commander.core.definition.ProcessDefinition;

public final class ProcessContext {
    private final ProcessId processId;
    private final ProcessDefinition processDefinition;

    protected ProcessContext(ProcessId processId, ProcessDefinition processDefinition) {
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
