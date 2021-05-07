package com.github.ynfeng.commander.raft.roles;

import com.github.ynfeng.commander.raft.RaftContext;
import com.github.ynfeng.commander.raft.Term;
import com.github.ynfeng.commander.raft.VoteTracker;
import com.github.ynfeng.commander.raft.protocol.RequestVote;
import com.github.ynfeng.commander.raft.protocol.RequestVoteResponse;

public abstract class AbstratRaftRole implements RaftRole {
    private final VoteTracker voteTracker = new VoteTracker();
    private final RaftContext raftContext;

    protected AbstratRaftRole(RaftContext raftContext) {
        this.raftContext = raftContext;
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

    protected VoteTracker voteTracker() {
        return voteTracker;
    }

    protected RaftContext raftContext() {
        return raftContext;
    }
}
