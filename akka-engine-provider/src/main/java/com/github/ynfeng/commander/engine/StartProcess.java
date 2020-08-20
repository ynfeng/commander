package com.github.ynfeng.commander.engine;

import com.github.ynfeng.commander.definition.ProcessDefinition;

public class StartProcess implements EngineCommand {
    private final ProcessDefinition processDefinition;
    private final ProcessFuture processFuture;

    public StartProcess(ProcessDefinition processDefinition, ProcessFuture processFuture) {
        this.processDefinition = processDefinition;
        this.processFuture = processFuture;
    }

    public ProcessDefinition processDefinition() {
        return processDefinition;
    }

    public ProcessFuture processFuture() {
        return processFuture;
    }
}
