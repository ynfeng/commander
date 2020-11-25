package com.github.ynfeng.commander.engine.command;

import com.github.ynfeng.commander.engine.ProcessFuture;
import com.github.ynfeng.commander.engine.Variables;

public class ContinueNodeExecute implements EngineCommand {
    private final String nodeRefName;
    private final Variables variables;
    private final ProcessFuture processFuture;

    public ContinueNodeExecute(String nodeRefName, Variables variables, ProcessFuture processFuture) {
        this.nodeRefName = nodeRefName;
        this.variables = variables;
        this.processFuture = processFuture;
    }

    public ProcessFuture processFuture() {
        return processFuture;
    }

    public String nodeRefName() {
        return nodeRefName;
    }

    public Variables variables() {
        return variables;
    }
}
