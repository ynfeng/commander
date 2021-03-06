package com.github.ynfeng.commander.engine;

import com.github.ynfeng.commander.definition.ProcessDefinition;
import com.github.ynfeng.commander.engine.executor.NodeExecutors;

public class ProcessInstanceBuilder {
    private final ReactorProcessInstance processInstance;

    protected ProcessInstanceBuilder() {
        processInstance = new ReactorProcessInstance();
    }

    public ReactorProcessInstance build() {
        return processInstance;
    }

    public ProcessInstanceBuilder processId(ProcessId processId) {
        processInstance.setProcessId(processId);
        return this;
    }

    public ProcessInstanceBuilder variables(Variables variables) {
        processInstance.setInput(variables);
        return this;
    }

    public ProcessInstanceBuilder processFuture(ProcessFuture processFuture) {
        processInstance.setProcessFuture(processFuture);
        return this;
    }

    public ProcessInstanceBuilder processDefinition(ProcessDefinition processDefinition) {
        processInstance.setProcessDefinition(processDefinition);
        return this;
    }

    public ProcessInstanceBuilder nodeExecutors(NodeExecutors nodeExecutors) {
        processInstance.setNodeExecutors(nodeExecutors);
        return this;
    }
}
