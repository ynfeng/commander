package com.github.ynfeng.commander.raft;

import com.github.ynfeng.commander.raft.protocol.Request;
import com.github.ynfeng.commander.raft.protocol.Response;
import java.util.concurrent.CompletableFuture;

public class RemoteMemberCommunicatorStub implements RemoteMemberCommunicator {
    @Override
    public <T extends Request, R extends Response> CompletableFuture<R> send(RemoteMember remoteMember, T request) {
        return null;
    }
}
