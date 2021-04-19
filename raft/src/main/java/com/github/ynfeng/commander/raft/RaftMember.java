package com.github.ynfeng.commander.raft;

public class RaftMember {
    private final MemberRole role;
    private final MemberId id;

    public RaftMember(MemberId id) {
        this.id = id;
        role = MemberRole.FLLOWER;
    }

    public boolean isFollower() {
        return role == MemberRole.FLLOWER;
    }

    public MemberId id() {
        return id;
    }
}
