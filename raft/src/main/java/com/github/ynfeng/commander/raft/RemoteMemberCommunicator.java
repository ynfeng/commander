package com.github.ynfeng.commander.raft;

import com.github.ynfeng.commander.raft.protocol.Request;
import com.github.ynfeng.commander.raft.protocol.Response;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public interface RemoteMemberCommunicator {
    <T extends Response> CompletableFuture<T> send(RemoteMember remoteMember, Request request);

    <T extends Request> void registerHandler(Class<T> requestType, Function<T, ? extends Response> action);
}
