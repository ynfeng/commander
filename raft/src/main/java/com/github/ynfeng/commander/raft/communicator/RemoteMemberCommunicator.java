package com.github.ynfeng.commander.raft.communicator;

import com.github.ynfeng.commander.raft.RemoteMember;
import com.github.ynfeng.commander.raft.protocol.Request;
import com.github.ynfeng.commander.raft.protocol.Response;
import com.github.ynfeng.commander.support.Manageable;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public interface RemoteMemberCommunicator extends Manageable {
    <T extends Response> CompletableFuture<T> send(RemoteMember remoteMember, Request request);

    <T extends Request> void registerHandler(Class<T> requestType, Function<T, ? extends Response> action);
}
