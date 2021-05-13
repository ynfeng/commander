package com.github.ynfeng.commander.raft;

import com.google.common.collect.Sets;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

public class VoteTracker {
    private final Set<MemberId> voters = Sets.newConcurrentHashSet();
    private final AtomicReference<VoteTo> vote = new AtomicReference<>();
    private final AtomicReference<Term> currentTerm = new AtomicReference<>();

    public boolean isQuorum(int quorum) {
        return voters.size() >= quorum;
    }

    public boolean isAlreadyVote(Term term, MemberId memberId) {
        if (vote.get() == null) {
            return false;
        }
        VoteTo voteTo = vote.get();
        return voteTo.term.equals(term) && memberId.equals(voteTo.memberId);
    }

    public void recordVote(Term term, MemberId memberId) {
        VoteTo voteTo = new VoteTo(term, memberId);
        vote.set(voteTo);
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
