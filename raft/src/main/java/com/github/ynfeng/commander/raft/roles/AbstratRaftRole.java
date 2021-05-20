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
        Term currentTerm = raftContext.currentTerm();

        if (voteRequest.lastLogIndex() < raftContext.lastLogIndex()
            || voteRequest.lastLogTerm().lessThan(raftContext.lastLogTerm())) {
            return RequestVoteResponse.declined(currentTerm, raftContext.localMermberId());
        }

        if (voteTracker().isAlreadyVoteTo(voteRequest.term(), voteRequest.candidateId())) {
            LOGGER.info("{} vote to {} at term {}", raftContext.localMermberId().id(), voteRequest.candidateId().id(), voteRequest.term().value());
            return RequestVoteResponse.voted(currentTerm, raftContext.localMermberId());
        }

        if (!voteTracker().canVote(voteRequest.term(), voteRequest.candidateId())) {
            return RequestVoteResponse.declined(currentTerm, raftContext.localMermberId());
        }

        if (voteRequest.term().lessThan(currentTerm)) {
            return RequestVoteResponse.declined(currentTerm, raftContext.localMermberId());
        }

        LOGGER.info("{} vote to {} at term {}", raftContext.localMermberId().id(), voteRequest.candidateId().id(), voteRequest.term().value());
        voteTracker().recordVoteCast(voteRequest.term(), voteRequest.candidateId());
        raftContext.tryUpdateCurrentTerm(voteRequest.term());

        return RequestVoteResponse.voted(currentTerm, raftContext.localMermberId());
    }

    protected boolean canVote(VoteRequest voteRequest) {
        Term currentTerm = raftContext.currentTerm();

        if (voteRequest.lastLogIndex() < raftContext.lastLogIndex()
            || voteRequest.lastLogTerm().lessThan(raftContext.lastLogTerm())) {
            return false;
        }

        if (voteTracker().isAlreadyVoteTo(voteRequest.term(), voteRequest.candidateId())) {
            return true;
        }

        if (!voteTracker().canVote(voteRequest.term(), voteRequest.candidateId())) {
            return false;
        }

        return voteRequest.term().greaterThan(currentTerm);
    }

    protected VoteTracker voteTracker() {
        return raftContext.voteTracker();
    }

    protected RaftContext raftContext() {
        return raftContext;
    }
}
