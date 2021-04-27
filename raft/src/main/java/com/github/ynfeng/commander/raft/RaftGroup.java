package com.github.ynfeng.commander.raft;

import com.google.common.collect.Lists;
import java.util.List;

public class RaftGroup {
    private final List<MemberId> memberIds = Lists.newArrayList();

    private RaftGroup() {
    }

    public static RaftGroup create() {
        return new RaftGroup();
    }

    public RaftGroup addMemberId(MemberId memberId) {
        memberIds.add(memberId);
        return this;
    }

    public int quorum() {
        return memberIds.size() / 2 + 1;
    }
}
