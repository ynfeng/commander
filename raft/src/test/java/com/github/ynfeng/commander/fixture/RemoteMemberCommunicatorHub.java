package com.github.ynfeng.commander.fixture;

import com.github.ynfeng.commander.raft.MemberId;
import com.github.ynfeng.commander.raft.RemoteMember;
import com.github.ynfeng.commander.raft.RemoteMemberCommunicator;
import com.github.ynfeng.commander.raft.protocol.Request;
import com.github.ynfeng.commander.raft.protocol.Response;
import com.google.common.collect.Maps;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public class RemoteMemberCommunicatorHub implements RemoteMemberCommunicator {
    private final Map<MemberId, FakeRemoteMemberCommunicator> communicators = Maps.newHashMap();

    @SuppressWarnings("unchecked")
    @Override
    public <T extends Response> CompletableFuture<T> send(RemoteMember remoteMember, Request request) {
        CompletableFuture<Response> future = new CompletableFuture<>();
        FakeRemoteMemberCommunicator fakeRemoteMemberCommunicator = communicators.get(remoteMember.id());
        Response response = fakeRemoteMemberCommunicator.receiveRequest(request);
        future.complete(response);
        return (CompletableFuture<T>) future;
    }

    @Override
    public <T extends Request> void registerHandler(Class<T> requestType, Function<T, ? extends Response> action) {

    }


    public void registerCommunicator(MemberId memberId, FakeRemoteMemberCommunicator communicator) {
        communicators.put(memberId, communicator);
    }

    public void reset() {
        communicators.clear();
    }
}
