package com.github.ynfeng.commander.raft;

import com.google.common.collect.Sets;
import java.util.Set;

public class VoteTracker {
    private final Set<MemberId> voters = Sets.newConcurrentHashSet();

    public void voteGranted(MemberId localMermberId) {
        voters.add(localMermberId);
    }

    public boolean isQuorum(int quorum) {
        return voters.size() >= quorum;
    }
}
