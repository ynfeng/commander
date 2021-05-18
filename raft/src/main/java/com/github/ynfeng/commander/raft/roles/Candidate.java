package com.github.ynfeng.commander.raft.roles;

import com.github.ynfeng.commander.raft.MemberId;
import com.github.ynfeng.commander.raft.RaftContext;
import com.github.ynfeng.commander.raft.RemoteMember;
import com.github.ynfeng.commander.raft.RemoteMemberCommunicator;
import com.github.ynfeng.commander.raft.protocol.LeaderHeartbeat;
import com.github.ynfeng.commander.raft.protocol.RequestVote;
import com.github.ynfeng.commander.raft.protocol.RequestVoteResponse;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class Candidate extends AbstratRaftRole {

    public Candidate(RaftContext raftContext) {
        super(raftContext);
    }

    @Override
    public void prepare() {
        raftContext().resumeElectionTimer();
        requestVote();
    }

    public void requestVote() {
        voteToSelf();
        askEachRemoteMemberToVote();
    }

    private void voteToSelf() {
        MemberId localMermberId = raftContext().localMermberId();
        voteTracker().voteToMe(localMermberId);
    }

    private void askEachRemoteMemberToVote() {
        List<RemoteMember> remoteMembers = raftContext().remoteMembers();
        remoteMembers.forEach(this::askToVote);
    }

    private void askToVote(RemoteMember remoteMember) {
        RemoteMemberCommunicator communicator = raftContext().remoteMemberCommunicator();
        RequestVote request = createVoteRequest();
        CompletableFuture<RequestVoteResponse> responseFuture = communicator.send(remoteMember, request);
        responseFuture.thenAccept(this::handleVoteResponse);
    }

    private RequestVote createVoteRequest() {
        return RequestVote.builder()
            .candidateId(raftContext().localMermberId())
            .lastLogTerm(raftContext().lastLogTerm())
            .lastLogIndex(raftContext().lastLogIndex())
            .term(raftContext().currentTerm())
            .build();
    }

    private void handleVoteResponse(RequestVoteResponse response) {
        if (response.isVoteGranted()) {
            voteTracker().voteToMe(response.voterId());
            if (voteTracker().isQuorum(raftContext().quorum())) {
                raftContext().becomeLeader();
            }
        } else {
            raftContext().tryUpdateCurrentTerm(response.term());
        }
    }

    @Override
    public void destory() {
        //do nothing
    }

    @Override
    public void handleHeartBeat(LeaderHeartbeat heartbeat) {
        if (heartbeat.isLegal(raftContext())) {
            raftContext().resetElectionTimer();
            raftContext().becomeFollower(heartbeat.leaderId());
        }
    }
}
