package com.github.ynfeng.commander.raft;

import com.google.common.collect.Lists;
import java.util.List;

public class MemberIds {
    private final List<MemberId> memberIds = Lists.newArrayList();

    private MemberIds() {
    }

    public MemberIds add(MemberId id) {
        memberIds.add(id);
        return this;
    }

    public static MemberIds create() {
        return new MemberIds();
    }
}
