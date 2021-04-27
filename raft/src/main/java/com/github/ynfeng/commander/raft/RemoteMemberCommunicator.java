package com.github.ynfeng.commander.raft;

import com.github.ynfeng.commander.raft.protocol.Request;
import com.github.ynfeng.commander.raft.protocol.Response;
import java.util.concurrent.CompletableFuture;

public interface RemoteMemberCommunicator {
    <REQ extends Request, RES extends Response> CompletableFuture<RES> send(RemoteMember remoteMember, REQ request);
}
