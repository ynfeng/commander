package com.github.ynfeng.commander.raft.roles;

import com.github.ynfeng.commander.raft.MemberId;
import com.github.ynfeng.commander.raft.RaftContext;
import com.github.ynfeng.commander.raft.RemoteMember;
import com.github.ynfeng.commander.raft.RemoteMemberCommunicator;
import com.github.ynfeng.commander.raft.Term;
import com.github.ynfeng.commander.raft.VoteTracker;
import com.github.ynfeng.commander.raft.protocol.LeaderHeartbeat;
import com.github.ynfeng.commander.raft.protocol.RequestVoteResponse;
import com.github.ynfeng.commander.raft.protocol.VoteRequest;
import com.github.ynfeng.commander.support.logger.CmderLoggerFactory;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import org.slf4j.Logger;

public class Candidate extends AbstratRaftRole {
    private static final Logger LOGGER = CmderLoggerFactory.getSystemLogger();
    private final VoteTracker voteTracker;

    public Candidate(RaftContext raftContext) {
        super(raftContext);
        voteTracker = raftContext.voteTracker();
        raftContext().nextTerm();
        voteToSelf();
    }

    @Override
    public void prepare() {
        raftContext().resetElectionTimer();
        raftContext().resumeElectionTimer();
        voteTracker.resetVotes();
        requestVote();
    }

    public void requestVote() {
        askEachRemoteMemberToVote();
    }

    private void voteToSelf() {
        MemberId localMermberId = raftContext().localMermberId();
        voteTracker.voteToMe(localMermberId);
        voteTracker.recordVoteCast(raftContext().currentTerm(), raftContext().localMermberId());
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
            voteTracker.recordVoteCast(voteRequest.term(), voteRequest.candidateId());
            raftContext.tryUpdateCurrentTerm(voteRequest.term());
            LOGGER.info("{} is candicate vote to {} at term {}",
                raftContext.localMermberId().id(), voteRequest.candidateId().id(), voteRequest.term().value());
            return RequestVoteResponse.voted(raftContext.currentTerm(), raftContext.localMermberId());
        }
        return RequestVoteResponse.declined(raftContext.currentTerm(), raftContext.localMermberId());
    }

    private boolean canVote(VoteRequest voteRequest) {
        RaftContext raftContext = raftContext();

        Term currentTerm = raftContext.currentTerm();

        if (voteRequest.term().lessThan(currentTerm)) {
            return false;
        }

        if (voteRequest.lastLogIndex() < raftContext.lastLogIndex()
            || voteRequest.lastLogTerm().lessThan(raftContext.lastLogTerm())) {
            return false;
        }

        if (voteTracker.isAlreadyVoteTo(voteRequest.term(), voteRequest.candidateId())) {
            return true;
        }

        return voteTracker.canVote(voteRequest.term(), voteRequest.candidateId());
    }

    private void handleVoteResponse(RequestVoteResponse response) {
        if (response.isVoted()) {
            voteTracker.voteToMe(response.voterId());
            if (voteTracker.isQuorum(raftContext().quorum())) {
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
            raftContext().becomeFollower(heartbeat.term(), heartbeat.leaderId());
        }
    }
}
