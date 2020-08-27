package com.github.ynfeng.commander.engine.command;

import com.github.ynfeng.commander.engine.executor.NodeExecutingVariable;
import java.util.concurrent.CompletableFuture;

public class SetNodeExecutingVariable implements EngineCommand {
    private final String key;
    private final Object val;
    private final CompletableFuture<NodeExecutingVariable> future;

    public SetNodeExecutingVariable(String key, Object val, CompletableFuture<NodeExecutingVariable> future) {
        this.key = key;
        this.val = val;
        this.future = future;
    }

    public CompletableFuture<NodeExecutingVariable> future() {
        return future;
    }

    public String key() {
        return key;
    }

    public Object value() {
        return val;
    }
}
