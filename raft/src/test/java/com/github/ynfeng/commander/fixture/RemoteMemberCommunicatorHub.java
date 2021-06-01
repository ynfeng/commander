package com.github.ynfeng.commander.fixture;

import com.github.ynfeng.commander.raft.MemberId;
import com.github.ynfeng.commander.raft.RemoteMember;
import com.github.ynfeng.commander.raft.RemoteMemberCommunicator;
import com.github.ynfeng.commander.raft.protocol.Request;
import com.github.ynfeng.commander.raft.protocol.Response;
import com.github.ynfeng.commander.support.logger.CmderLoggerFactory;
import com.google.common.collect.Maps;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Function;
import org.slf4j.Logger;

public class RemoteMemberCommunicatorHub implements RemoteMemberCommunicator {
    private static final Logger LOGGER = CmderLoggerFactory.getSystemLogger();
    private final Map<MemberId, FakeRemoteMemberCommunicator> communicators = Maps.newHashMap();
    private final ExecutorService threadPool;

    public RemoteMemberCommunicatorHub() {
        threadPool = Executors.newFixedThreadPool(2,
            new ThreadFactoryBuilder()
                .setUncaughtExceptionHandler((t, e) -> LOGGER.error("remote member communicator hub error.", e))
                .setNameFormat("remote-member-communicator-hub-thread-%d")
                .build());
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends Response> CompletableFuture<T> send(RemoteMember remoteMember, Request request) {
        CompletableFuture<Response> future = new CompletableFuture<>();
        FakeRemoteMemberCommunicator fakeRemoteMemberCommunicator = communicators.get(remoteMember.id());
        threadPool.execute(() -> {
            Response response = fakeRemoteMemberCommunicator.receiveRequest(request);
            future.complete(response);
        });
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
