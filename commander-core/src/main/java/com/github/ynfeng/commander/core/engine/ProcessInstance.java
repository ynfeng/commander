package com.github.ynfeng.commander.core.engine;

import com.github.ynfeng.commander.core.definition.ProcessDefinition;

public final class ProcessInstance {
    private final ProcessId processId;
    private final ProcessDefinition processDefinition;
    private ProcessStatus processStatus;

    public ProcessInstance(ProcessId processId, ProcessDefinition processDefinition) {
        this.processId = processId;
        this.processDefinition = processDefinition;
    }

    public static ProcessInstance create(ProcessId processId, ProcessDefinition processDefinition) {
        return new ProcessInstance(processId, processDefinition);
    }

    public ProcessStatus status() {
        return processStatus;
    }

    public void complete() {
        processStatus = ProcessStatus.COMPLETED;
    }
}
