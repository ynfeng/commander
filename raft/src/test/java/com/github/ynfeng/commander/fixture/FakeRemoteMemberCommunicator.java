package com.github.ynfeng.commander.fixture;

import com.github.ynfeng.commander.raft.MemberId;
import com.github.ynfeng.commander.raft.RemoteMember;
import com.github.ynfeng.commander.raft.RemoteMemberCommunicator;
import com.github.ynfeng.commander.raft.Term;
import com.github.ynfeng.commander.raft.protocol.LeaderHeartbeat;
import com.github.ynfeng.commander.raft.protocol.Request;
import com.github.ynfeng.commander.raft.protocol.Response;
import com.google.common.collect.Maps;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

public class FakeRemoteMemberCommunicator implements RemoteMemberCommunicator {
    private final RemoteMemberCommunicatorHub hub;
    private final Map<Class<? extends Request>, Function<? extends Request, ? extends Response>> handlers = Maps.newHashMap();
    private final Object watitLock = new Object();
    private final AtomicInteger leaderHeartbeatTimes = new AtomicInteger();
    private final AtomicReference<LeaderHeartbeatRecord> lastLeaderHeartBeatHolder = new AtomicReference<>(new LeaderHeartbeatRecord(Term.create(0), MemberId.create("none")));

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

    public Response receiveRequest(Request request) {
        LeaderHeartbeatRecord lastLeaderHeartbeat = lastLeaderHeartBeatHolder.get();
        if (request instanceof LeaderHeartbeat) {
            LeaderHeartbeat heartbeat = (LeaderHeartbeat) request;
            if (lastLeaderHeartbeat.isSame(heartbeat.term(), heartbeat.leaderId())) {
                int times = leaderHeartbeatTimes.incrementAndGet();
                if (times == 5) {
                    synchronized (watitLock) {
                        watitLock.notifyAll();
                    }
                }
            } else {
                leaderHeartbeatTimes.set(0);
            }
            lastLeaderHeartBeatHolder.set(new LeaderHeartbeatRecord(heartbeat.term(), heartbeat.leaderId()));
        }
        Function<Request, Response> function = (Function<Request, Response>) handlers.get(request.getClass());
        return function.apply(request);
    }

    public void expectLeader() throws InterruptedException {
        if (leaderHeartbeatTimes.get() >= 5) {
            return;
        }
        synchronized (watitLock) {
            watitLock.wait();
        }
    }
}
