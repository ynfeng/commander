package com.github.ynfeng.commander.raft.roles;

import com.github.ynfeng.commander.raft.MemberId;
import com.github.ynfeng.commander.raft.RaftContext;
import com.github.ynfeng.commander.raft.RemoteMember;
import com.github.ynfeng.commander.raft.RemoteMemberCommunicator;
import com.github.ynfeng.commander.raft.protocol.LeaderHeartbeat;
import com.github.ynfeng.commander.raft.protocol.RequestVoteResponse;
import com.github.ynfeng.commander.raft.protocol.VoteRequest;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class Candidate extends AbstratRaftRole {

    public Candidate(RaftContext raftContext) {
        super(raftContext);
    }

    @Override
    public void prepare() {
        raftContext().voteTracker().resetVotes();
        raftContext().resumeElectionTimer();
        raftContext().nextTerm();
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
        VoteRequest request = createVoteRequest();
        CompletableFuture<RequestVoteResponse> responseFuture = communicator.send(remoteMember, request);
        responseFuture.thenAccept(this::handleVoteResponse);
    }

    private VoteRequest createVoteRequest() {
        return VoteRequest.builder()
            .candidateId(raftContext().localMermberId())
            .lastLogTerm(raftContext().lastLogTerm())
            .lastLogIndex(raftContext().lastLogIndex())
            .term(raftContext().currentTerm())
            .build();
    }

    @Override
    public synchronized RequestVoteResponse handleRequestVote(VoteRequest voteRequest) {
        RaftContext raftContext = raftContext();
        if (canVote(voteRequest)) {
            raftContext.voteTracker().recordVoteCast(voteRequest.term(), voteRequest.candidateId());
            raftContext.tryUpdateCurrentTerm(voteRequest.term());
            raftContext.becomeCandidate();
            return RequestVoteResponse.voted(raftContext.currentTerm(), raftContext.localMermberId());
        }
        return RequestVoteResponse.declined(raftContext.currentTerm(), raftContext.localMermberId());
    }

    private void handleVoteResponse(RequestVoteResponse response) {
        if (response.isVoted()) {
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
        if (heartbeat.term().greaterOrEqual(raftContext().currentTerm())) {
            raftContext().resetElectionTimer();
            raftContext().becomeFollower(heartbeat.leaderId());
        }
    }
}
