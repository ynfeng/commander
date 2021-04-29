package com.github.ynfeng.commander.raft;

import com.github.ynfeng.commander.raft.protocol.LeaderHeartbeat;
import com.github.ynfeng.commander.raft.protocol.Request;
import com.github.ynfeng.commander.raft.protocol.RequestVote;
import com.github.ynfeng.commander.raft.protocol.RequestVoteResponse;
import com.github.ynfeng.commander.raft.protocol.Response;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class RemoteMemberCommunicatorStub implements RemoteMemberCommunicator {
    private Consumer<LeaderHeartbeat> heartbeatConsumer;

    public <T extends Request, R extends Response> CompletableFuture<R> send(RemoteMember remoteMember, T request) {
        if (request instanceof RequestVote) {
            CompletableFuture<RequestVoteResponse> completableFuture = new CompletableFuture<>();
            RequestVoteResponse response = RequestVoteResponse.granted(Term.create(0), MemberId.create(UUID.randomUUID().toString()));
            completableFuture.complete(response);
            return (CompletableFuture<R>) completableFuture;
        }

        if (request instanceof LeaderHeartbeat) {
            heartbeatConsumer.accept((LeaderHeartbeat) request);
        }

        return new CompletableFuture<>();
    }

    public void onReceiveHearbeat(Consumer<LeaderHeartbeat> heartbeatConsumer) {
        this.heartbeatConsumer = heartbeatConsumer;
    }

}
