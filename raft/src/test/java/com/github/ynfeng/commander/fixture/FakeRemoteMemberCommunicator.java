package com.github.ynfeng.commander.fixture;

import com.github.ynfeng.commander.raft.MemberId;
import com.github.ynfeng.commander.raft.RemoteMember;
import com.github.ynfeng.commander.raft.communicator.RemoteMemberCommunicator;
import com.github.ynfeng.commander.raft.protocol.Request;
import com.github.ynfeng.commander.raft.protocol.Response;
import com.google.common.collect.Maps;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public class FakeRemoteMemberCommunicator implements RemoteMemberCommunicator {
    private final RemoteMemberCommunicatorHub hub;
    private final Map<Class<? extends Request>, Function<? extends Request, ? extends Response>> handlers = Maps.newHashMap();
    private final MemberId memberId;
    private final RemoteMemberCommunicatorSpy spy;

    public FakeRemoteMemberCommunicator(RemoteMemberCommunicatorHub hub, MemberId memberId, RemoteMemberCommunicatorSpy spy) {
        this.hub = hub;
        this.memberId = memberId;
        this.spy = spy;
        this.spy.reset();
    }

    @Override
    public <T extends Response> CompletableFuture<T> send(RemoteMember remoteMember, Request request) {
        return hub.send(remoteMember, request);
    }

    @Override
    public <T extends Request> void registerHandler(Class<T> requestType, Function<T, ? extends Response> action) {
        handlers.put(requestType, action);
    }

    public Response receiveRequest(Request request) {
        Function<Request, Response> function = (Function<Request, Response>) handlers.get(request.getClass());
        spy.receiveRequest(request);
        return function.apply(request);
    }

    public MemberId getMemberId() {
        return memberId;
    }

    @Override
    public void start() {

    }

    @Override
    public void shutdown() {

    }

    @Override
    public boolean isStarted() {
        return false;
    }
}
