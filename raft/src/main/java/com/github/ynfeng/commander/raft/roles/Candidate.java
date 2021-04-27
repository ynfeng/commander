package com.github.ynfeng.commander.raft.roles;

import com.github.ynfeng.commander.raft.RaftContext;
import com.github.ynfeng.commander.raft.RemoteMember;
import com.github.ynfeng.commander.raft.RemoteMemberCommunicator;
import com.github.ynfeng.commander.raft.VoteTracker;
import com.github.ynfeng.commander.raft.protocol.RequestVote;
import com.github.ynfeng.commander.raft.protocol.RequestVoteResponse;
import java.util.concurrent.CompletableFuture;

public class Candidate implements RaftRole {
    private final RaftContext raftContext;
    private final VoteTracker voteTracker = new VoteTracker();

    public Candidate(RaftContext raftContext) {
        this.raftContext = raftContext;
    }

    @Override
    public void prepare() {
        //do nothing
    }

    @Override
    public void requestVote() {
        voteToSelf();
        askEachRemoteMemberToVote();
    }

    @Override
    public void destory() {
        //do nothing
    }

    private void voteToSelf() {
        voteTracker.voteGranted(raftContext.localMermberId());
    }

    private void askEachRemoteMemberToVote() {
        raftContext.remoteMembers().forEach(this::askToVote);
    }

    private void askToVote(RemoteMember remoteMember) {
        RemoteMemberCommunicator communicator = raftContext.remoteMemberCommunicator();
        RequestVote request = createVoteRequest();
        CompletableFuture<RequestVoteResponse> responseFuture = communicator.send(remoteMember, request);
        responseFuture.thenAccept(this::handleVoteResponse);
    }

    private void handleVoteResponse(RequestVoteResponse response) {
        if (response.isVoteGranted()) {
            voteTracker.voteGranted(response.voterId());
            if (voteTracker.isQuorum(raftContext.quorum())) {
                raftContext.becomeLeader();
            }
        } else {
            raftContext.tryUpdateCurrentTerm(response.term());
        }
    }

    private RequestVote createVoteRequest() {
        return RequestVote.builder()
            .candidateId(raftContext.localMermberId())
            .lastLogTerm(raftContext.lastLogTerm())
            .lastLogIndex(raftContext.lastLogIndex())
            .term(raftContext.currentTerm())
            .build();
    }
}
