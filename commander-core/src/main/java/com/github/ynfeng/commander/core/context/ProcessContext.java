package com.github.ynfeng.commander.core.context;


import com.github.ynfeng.commander.core.definition.ProcessDefinition;
import com.github.ynfeng.commander.core.engine.ProcessId;
import com.github.ynfeng.commander.core.engine.ProcessInstance;

public final class ProcessContext {
    private final ProcessId processId;
    private final ProcessDefinition processDefinition;
    private ProcessInstance processInstance;

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

    public ProcessInstance processInstance() {
        return processInstance;
    }

    public void attachProcessInstance(ProcessInstance processInstance) {
        this.processInstance = processInstance;

    }
}
