package com.github.ynfeng.commander.raft;

import com.google.common.collect.Sets;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

public class VoteTracker {
    private final Set<MemberId> voters = Sets.newConcurrentHashSet();
    private final AtomicReference<MemberId> voteTo = new AtomicReference<>();

    public void getMemberVote(MemberId localMermberId) {
        voters.add(localMermberId);
    }

    public boolean isQuorum(int quorum) {
        return voters.size() >= quorum;
    }

    public void votedTo(MemberId candidateId) {
        voteTo.set(candidateId);
    }

    public boolean isVotedFor(MemberId candidateId) {
        if (voteTo.get() == null) {
            return false;
        }

        return voteTo.get().equals(candidateId);
    }

    public boolean hasVoted() {
        return voteTo.get() != null;
    }

    public void reset() {
        voteTo.set(null);
        voters.clear();
    }
}
