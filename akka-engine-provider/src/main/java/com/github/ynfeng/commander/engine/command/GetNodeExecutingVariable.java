package com.github.ynfeng.commander.engine.command;

import com.github.ynfeng.commander.engine.executor.NodeExecutingVariable;
import java.util.concurrent.CompletableFuture;

public class GetNodeExecutingVariable implements EngineCommand {
    private final CompletableFuture<NodeExecutingVariable> future;

    public GetNodeExecutingVariable(CompletableFuture<NodeExecutingVariable> future) {
        this.future = future;
    }

    public CompletableFuture<NodeExecutingVariable> future() {
        return future;
    }
}
