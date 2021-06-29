package com.github.ynfeng.commander.fixture;

import com.github.ynfeng.commander.raft.RemoteMember;
import com.github.ynfeng.commander.raft.communicator.NettyRemoteMemberCommunicator;
import com.github.ynfeng.commander.raft.communicator.RemoteMemberCommunicator;
import com.github.ynfeng.commander.raft.protocol.Request;
import com.github.ynfeng.commander.raft.protocol.Response;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public class NettyRemoteMemberCommunicatorProxy implements RemoteMemberCommunicator {
    private final RemoteMemberCommunicatorSpy spy;
    private final NettyRemoteMemberCommunicator nettyRemoteMemberCommunicator;

    public NettyRemoteMemberCommunicatorProxy(RemoteMemberCommunicatorSpy spy, NettyRemoteMemberCommunicator nettyRemoteMemberCommunicator) {
        this.spy = spy;
        this.nettyRemoteMemberCommunicator = nettyRemoteMemberCommunicator;
    }

    @Override
    public <T extends Response> CompletableFuture<T> send(RemoteMember remoteMember, Request request) {
        return nettyRemoteMemberCommunicator.send(remoteMember, request);
    }

    @Override
    public <T extends Request> void registerHandler(Class<T> requestType, Function<T, ? extends Response> action) {
        nettyRemoteMemberCommunicator.registerHandler(requestType, request -> handle(request, action));
    }

    private <T extends Request> Response handle(T request, Function<T, ? extends Response> action) {
        spy.receiveRequest(request);
        return action.apply(request);
    }

    @Override
    public void start() {
        nettyRemoteMemberCommunicator.start();
    }

    @Override
    public void shutdown() {
        nettyRemoteMemberCommunicator.shutdown();
    }

    @Override
    public boolean isStarted() {
        return nettyRemoteMemberCommunicator.isStarted();
    }
}
