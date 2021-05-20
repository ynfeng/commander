package com.github.ynfeng.commander.raft.roles;

import com.github.ynfeng.commander.raft.RaftContext;
import com.github.ynfeng.commander.raft.Term;
import com.github.ynfeng.commander.raft.VoteTracker;
import com.github.ynfeng.commander.raft.protocol.RequestVoteResponse;
import com.github.ynfeng.commander.raft.protocol.VoteRequest;
import com.github.ynfeng.commander.support.logger.CmderLoggerFactory;
import org.slf4j.Logger;

public abstract class AbstratRaftRole implements RaftRole {
    private static final Logger LOGGER = CmderLoggerFactory.getSystemLogger();
    private final RaftContext raftContext;

    protected AbstratRaftRole(RaftContext raftContext) {
        this.raftContext = raftContext;
    }

    @SuppressWarnings( {"MethodLength"})
    @Override
    public synchronized RequestVoteResponse handleRequestVote(VoteRequest voteRequest) {
        if (canVote(voteRequest)) {
            raftContext.voteTracker().recordVoteCast(voteRequest.term(), voteRequest.candidateId());
            raftContext.tryUpdateCurrentTerm(voteRequest.term());
            raftContext.becomeCandidate();
            return RequestVoteResponse.voted(raftContext.currentTerm(), raftContext.localMermberId());
        }
        return RequestVoteResponse.declined(raftContext.currentTerm(), raftContext.localMermberId());
    }

    protected boolean canVote(VoteRequest voteRequest) {
        Term currentTerm = raftContext.currentTerm();

        if (voteRequest.term().lessThan(currentTerm)) {
            return false;
        }

        if (voteRequest.lastLogIndex() < raftContext.lastLogIndex()
            || voteRequest.lastLogTerm().lessThan(raftContext.lastLogTerm())) {
            return false;
        }

        if (voteTracker().isAlreadyVoteTo(voteRequest.term(), voteRequest.candidateId())) {
            return true;
        }

        return voteTracker().canVote(voteRequest.term(), voteRequest.candidateId());
    }

    protected VoteTracker voteTracker() {
        return raftContext.voteTracker();
    }

    protected RaftContext raftContext() {
        return raftContext;
    }
}
