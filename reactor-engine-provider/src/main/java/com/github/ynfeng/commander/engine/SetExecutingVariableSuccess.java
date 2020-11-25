package com.github.ynfeng.commander.engine;

import com.github.ynfeng.commander.engine.command.EngineCommand;
import com.github.ynfeng.commander.engine.executor.NodeExecutingVariable;
import java.util.concurrent.CompletableFuture;

public class SetExecutingVariableSuccess implements EngineCommand {
    private final CompletableFuture<NodeExecutingVariable> future;
    private final NodeExecutingVariable variable;

    public SetExecutingVariableSuccess(CompletableFuture<NodeExecutingVariable> future,
                                       NodeExecutingVariable variable) {

        this.future = future;
        this.variable = variable;
    }

    public CompletableFuture<NodeExecutingVariable> future() {
        return future;
    }

    public final NodeExecutingVariable variable() {
        return variable;
    }
}
