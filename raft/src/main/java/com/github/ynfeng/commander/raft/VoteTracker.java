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
        VoteTo voteTo = this.voteTo.get();
        return voteTo.term.equals(term) && memberId.equals(voteTo.memberId);
    }

    public void recordVoteCast(Term term, MemberId memberId) {
        VoteTo voteTo = new VoteTo(term, memberId);
        this.voteTo.set(voteTo);
    }

    public void voteToMe(MemberId memberId) {
        votes.add(memberId);
    }

    public boolean isVoteToMe(MemberId memberId) {
        return votes.contains(memberId);
    }

    public boolean canVote(Term term, MemberId memberId) {
        VoteTo voteTo = this.voteTo.get();
        if (voteTo == null) {
            return true;
        }
        if (isAlreadyVoteTo(term, memberId)) {
            return true;
        }
        return voteTo.term.lessThan(term);
    }

    public void resetAll() {
        votes.clear();
        voteTo.set(null);
    }

    public void resetVotes() {
        votes.clear();
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
