package com.github.ynfeng.commander.raft;

import com.github.ynfeng.commander.raft.protocol.Request;
import com.github.ynfeng.commander.raft.protocol.Response;
import com.google.common.collect.Maps;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class RemoteMemberCommunicatorHub implements RemoteMemberCommunicator {
    private final Map<MemberId, RemoteMemberCommunicator> communicators = Maps.newHashMap();

    @Override
    public <T extends Request, R extends Response> CompletableFuture<R> send(RemoteMember remoteMember, T request) {
        return communicators.get(remoteMember.id()).send(remoteMember, request);
    }

    public void addCommunicator(MemberId memberId, RemoteMemberCommunicator communicator) {
        communicators.put(memberId, communicator);
    }

    public void reset() {
        communicators.clear();
    }
}
