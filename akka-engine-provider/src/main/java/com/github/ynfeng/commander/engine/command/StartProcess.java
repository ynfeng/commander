package com.github.ynfeng.commander.engine.command;

import com.github.ynfeng.commander.definition.ProcessDefinition;
import com.github.ynfeng.commander.engine.ProcessFuture;
import com.github.ynfeng.commander.engine.Variables;

public class StartProcess implements EngineCommand {
    private final ProcessDefinition processDefinition;
    private final ProcessFuture processFuture;
    private final Variables variables;

    public StartProcess(ProcessDefinition processDefinition,
                        ProcessFuture processFuture,
                        Variables variables) {
        this.processDefinition = processDefinition;
        this.processFuture = processFuture;
        this.variables = variables;
    }

    public ProcessDefinition processDefinition() {
        return processDefinition;
    }

    public ProcessFuture processFuture() {
        return processFuture;
    }

    public Variables variables() {
        return variables;
    }
}
