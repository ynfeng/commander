package com.github.ynfeng.commander._fixture;

import com.github.ynfeng.commander.raft.RemoteMember;
import com.github.ynfeng.commander.raft.RemoteMemberCommunicator;
import com.github.ynfeng.commander.raft.protocol.Request;
import com.github.ynfeng.commander.raft.protocol.Response;
import com.google.common.collect.Maps;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public class FakeRemoteMemberCommunicator implements RemoteMemberCommunicator {
    private final RemoteMemberCommunicatorHub hub;
    private final Map<Class<? extends Request>, Function<? extends Request, ? extends Response>> handlers = Maps.newHashMap();
    private final Object lock = new Object();
    private Request receivedRequest;

    public FakeRemoteMemberCommunicator(RemoteMemberCommunicatorHub hub) {
        this.hub = hub;
    }

    @Override
    public <T extends Response> CompletableFuture<T> send(RemoteMember remoteMember, Request request) {
        return hub.send(remoteMember, request);
    }

    @Override
    public <T extends Request> void registerHandler(Class<T> requestType, Function<T, ? extends Response> action) {
        handlers.put(requestType, action);
    }

    @SuppressWarnings("unchecked")
    public Response receiveRequest(Request request) {
        synchronized (lock) {
            receivedRequest = request;
            lock.notifyAll();
        }
        Function<Request, Response> function = (Function<Request, Response>) handlers.get(request.getClass());
        return function.apply(request);
    }

    @SuppressWarnings("unchecked")
    public <T extends Request> T expectRequest(Class<T> requestType) {
        while (true) {
            synchronized (lock) {
                try {
                    lock.wait();
                    if (receivedRequest.getClass().equals(requestType)) {
                        return (T) receivedRequest;
                    }
                } catch (InterruptedException ignored) {
                }
            }
        }
    }
}
