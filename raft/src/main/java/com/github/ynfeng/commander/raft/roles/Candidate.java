package com.github.ynfeng.commander.raft.roles;

import com.github.ynfeng.commander.raft.RaftContext;
import com.github.ynfeng.commander.raft.RemoteMember;
import com.github.ynfeng.commander.raft.RemoteMemberCommunicator;
import com.github.ynfeng.commander.raft.Term;
import com.github.ynfeng.commander.raft.VoteTracker;
import com.github.ynfeng.commander.raft.protocol.RequestVote;
import com.github.ynfeng.commander.raft.protocol.RequestVoteResponse;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class Candidate implements RaftRole {
    private final RaftContext raftContext;
    private final VoteTracker voteTracker = new VoteTracker();

    public Candidate(RaftContext raftContext) {
        this.raftContext = raftContext;
    }

    @Override
    public void prepare() {
        requestVote();
    }

    public void requestVote() {
        voteToSelf();
        askEachRemoteMemberToVote();
    }

    private void voteToSelf() {
        voteTracker.getMemberVote(raftContext.localMermberId());
    }

    private void askEachRemoteMemberToVote() {
        List<RemoteMember> remoteMembers = raftContext.remoteMembers();
        remoteMembers.forEach(this::askToVote);
    }

    private void askToVote(RemoteMember remoteMember) {
        RemoteMemberCommunicator communicator = raftContext.remoteMemberCommunicator();
        RequestVote request = createVoteRequest();
        CompletableFuture<RequestVoteResponse> responseFuture = communicator.send(remoteMember, request);
        responseFuture.thenAccept(this::handleVoteResponse);
    }

    private RequestVote createVoteRequest() {
        return RequestVote.builder()
            .candidateId(raftContext.localMermberId())
            .lastLogTerm(raftContext.lastLogTerm())
            .lastLogIndex(raftContext.lastLogIndex())
            .term(raftContext.currentTerm())
            .build();
    }

    private void handleVoteResponse(RequestVoteResponse response) {
        if (response.isVoteGranted()) {
            voteTracker.getMemberVote(response.voterId());
            if (voteTracker.isQuorum(raftContext.quorum())) {
                raftContext.becomeLeader();
            }
        } else {
            raftContext.tryUpdateCurrentTerm(response.term());
        }
    }

    @Override
    public void destory() {
        //do nothing
    }

    @Override
    public RequestVoteResponse handleRequestVote(RequestVote requestVote) {
        Term currentTerm = raftContext.currentTerm();

        if (voteTracker.hasVoted() && voteTracker.isVotedFor(requestVote.candidateId())) {
            return RequestVoteResponse.voted(currentTerm, raftContext.localMermberId());
        }

        if (voteTracker.hasVoted()) {
            return RequestVoteResponse.declined(currentTerm, raftContext.localMermberId());
        }

        if (requestVote.term().lessThan(currentTerm)) {
            return RequestVoteResponse.declined(currentTerm, raftContext.localMermberId());
        }

        voteTracker.votedFor(requestVote.candidateId());
        return RequestVoteResponse.voted(currentTerm, raftContext.localMermberId());
    }
}
