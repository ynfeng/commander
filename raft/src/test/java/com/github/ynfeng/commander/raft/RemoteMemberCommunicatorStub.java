package com.github.ynfeng.commander.raft;

import com.github.ynfeng.commander.raft.protocol.Request;
import com.github.ynfeng.commander.raft.protocol.Response;
import java.util.concurrent.CompletableFuture;

public class RemoteMemberCommunicatorStub implements RemoteMemberCommunicator {
    @Override
    public <REQ extends Request, RES extends Response> CompletableFuture<RES> send(RemoteMember remoteMember, REQ request) {
        return null;
    }
}
