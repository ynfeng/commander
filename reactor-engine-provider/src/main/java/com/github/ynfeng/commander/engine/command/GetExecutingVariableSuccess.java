package com.github.ynfeng.commander.engine.command;

import com.github.ynfeng.commander.engine.executor.NodeExecutingVariable;
import java.util.concurrent.CompletableFuture;

public class GetExecutingVariableSuccess implements EngineCommand {
    private final CompletableFuture<NodeExecutingVariable> future;
    private NodeExecutingVariable variable;

    public GetExecutingVariableSuccess(CompletableFuture<NodeExecutingVariable> future,
                                       NodeExecutingVariable variable) {
        this.future = future;
        this.variable = variable;
    }

    public CompletableFuture<NodeExecutingVariable> future() {
        return future;
    }

    public NodeExecutingVariable variable() {
        return variable;
    }
}
