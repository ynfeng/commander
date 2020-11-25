package com.github.ynfeng.commander.engine;

public class ProcessInstanceActorBuilder {
    private final ProcessInstanceActor processInstance;

    public ProcessInstanceActorBuilder(ProcessInstanceActor processInstance) {
        this.processInstance = processInstance;
    }

    public ProcessInstanceActor build() {
        return processInstance;
    }

    public ProcessInstanceActorBuilder processId(ProcessId processId) {
        processInstance.setProcessId(processId);
        return this;
    }

    public ProcessInstanceActorBuilder variables(Variables variables) {
        processInstance.setInput(variables);
        return this;
    }

    public ProcessInstanceActorBuilder processFuture(ProcessFuture processFuture) {
        processInstance.setProcessFuture(processFuture);
        return this;
    }

    public ProcessInstanceActorBuilder environment(EngineEnvironment environment) {
        processInstance.setEnvironment(environment);
        return this;
    }
}
