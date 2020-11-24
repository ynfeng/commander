package com.github.ynfeng.commander.engine.command;

import com.github.ynfeng.commander.engine.ProcessFuture;
import com.github.ynfeng.commander.engine.ProcessId;
import com.github.ynfeng.commander.engine.Variables;

public class ContinueProcess implements EngineCommand {
    private final ProcessId processId;
    private final String nodeRefName;
    private final Variables variables;
    private final ProcessFuture processFuture;

    public ContinueProcess(ProcessId processId, String nodeRefName, Variables variables) {
        this.processId = processId;
        this.nodeRefName = nodeRefName;
        this.variables = variables;
        processFuture = new ProcessFuture();
    }

    public ProcessFuture processFuture() {
        return processFuture;
    }

    public ProcessId processId() {
        return processId;
    }

    public String nodeRefName() {
        return nodeRefName;
    }

    public Variables variables() {
        return variables;
    }
}
