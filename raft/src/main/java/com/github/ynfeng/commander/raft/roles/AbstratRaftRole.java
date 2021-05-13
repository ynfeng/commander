package com.github.ynfeng.commander.raft.roles;

import com.github.ynfeng.commander.raft.RaftContext;
import com.github.ynfeng.commander.raft.Term;
import com.github.ynfeng.commander.raft.VoteTracker;
import com.github.ynfeng.commander.raft.protocol.RequestVote;
import com.github.ynfeng.commander.raft.protocol.RequestVoteResponse;
import com.github.ynfeng.commander.support.logger.CmderLoggerFactory;
import org.slf4j.Logger;

public abstract class AbstratRaftRole implements RaftRole {
    private static final Logger LOGGER = CmderLoggerFactory.getSystemLogger();
    private final VoteTracker voteTracker = new VoteTracker();
    private final RaftContext raftContext;

    protected AbstratRaftRole(RaftContext raftContext) {
        this.raftContext = raftContext;
    }

    @SuppressWarnings( {"checkstyle:CyclomaticComplexity", "checkstyle:MethodLength"})
    @Override
    public synchronized RequestVoteResponse handleRequestVote(RequestVote requestVote) {
//        Term currentTerm = raftContext.currentTerm();
//        resetVoteTrackerIfNewTerm(requestVote.term());
//
//        if (requestVote.lastLogIndex() < raftContext.lastLogIndex()
//            || requestVote.lastLogTerm().lessThan(raftContext.lastLogTerm())) {
//            return RequestVoteResponse.declined(currentTerm, raftContext.localMermberId());
//        }
//
//        if (voteTracker.isVotedFor(requestVote.candidateId())) {
//            LOGGER.info("{} vote to {}", raftContext.localMermberId().id(), requestVote.candidateId().id());
//            return RequestVoteResponse.voted(currentTerm, raftContext.localMermberId());
//        }
//
//        if (voteTracker.hasVoted()) {
//            return RequestVoteResponse.declined(currentTerm, raftContext.localMermberId());
//        }
//
//        if (requestVote.term().lessThan(currentTerm)) {
//            return RequestVoteResponse.declined(currentTerm, raftContext.localMermberId());
//        }
//
//        LOGGER.info("{} vote to {}", raftContext.localMermberId().id(), requestVote.candidateId().id());
//        voteTracker.recordVote(requestVote.candidateId());
//        raftContext.tryUpdateCurrentTerm(requestVote.term());
        return RequestVoteResponse.voted(Term.create(0), raftContext.localMermberId());
    }

    private void resetVoteTrackerIfNewTerm(Term requestTerm) {
//        Term currentTerm = raftContext.currentTerm();
//        if (requestTerm.greaterThan(currentTerm)) {
//            voteTracker.reset();
//        }
    }

    protected VoteTracker voteTracker() {
        return voteTracker;
    }

    protected RaftContext raftContext() {
        return raftContext;
    }
}
