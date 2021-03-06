package com.github.ynfeng.commander.raft;

import com.google.common.collect.Sets;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

public class VoteTracker {
    private final AtomicReference<VoteTo> voteTo = new AtomicReference<>();
    private final Set<MemberId> votes = Sets.newConcurrentHashSet();

    public boolean isQuorum(int quorum) {
        return votes.size() >= quorum;
    }

    public boolean isAlreadyVoteTo(Term term, MemberId memberId) {
        if (voteTo.get() == null) {
            return false;
        }
        VoteTo vote = voteTo.get();
        return vote.term.equals(term) && memberId.equals(vote.memberId);
    }

    public void recordVoteCast(Term term, MemberId memberId) {
        VoteTo vote = new VoteTo(term, memberId);
        voteTo.set(vote);
    }

    public void voteToMe(MemberId memberId) {
        votes.add(memberId);
    }

    public boolean isVoteToMe(MemberId memberId) {
        return votes.contains(memberId);
    }

    public boolean canVote(Term term, MemberId memberId) {
        VoteTo vote = voteTo.get();
        if (vote == null) {
            return true;
        }
        if (isAlreadyVoteTo(term, memberId)) {
            return true;
        }
        return vote.term.lessThan(term);
    }

    public void reset() {
        votes.clear();
        voteTo.set(null);
    }

    public boolean termIsVoted(Term term) {
        VoteTo vote = voteTo.get();
        if (vote == null) {
            return false;
        }
        return vote.term.equals(term);
    }

    static class VoteTo {
        private final Term term;
        private final MemberId memberId;

        VoteTo(Term term, MemberId memberId) {
            this.memberId = memberId;
            this.term = term;
        }
    }
}
