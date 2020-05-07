package com.github.ynfeng.commander.core.engine;


import com.github.ynfeng.commander.core.definition.ProcessDefinition;

public class ProcessEngine {
    private final ProcessIdGenerator processIdGenerator;
    private final ProcessContexts processContexts = new ProcessContexts();

    public ProcessEngine(ProcessIdGenerator processIdGenerator) {
        this.processIdGenerator = processIdGenerator;
    }

    public ProcessId startProcess(ProcessDefinition processDefinition) {
        ProcessId processId = processIdGenerator.nextId();
        ProcessContext processContext = new ProcessContext(processId, processDefinition);
        processContexts.add(processContext);
        return processId;
    }

    public ProcessContext processContext(ProcessId processId) {
        return processContexts.get(processId);
    }
}
