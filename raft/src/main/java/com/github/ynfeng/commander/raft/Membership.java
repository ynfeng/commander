package com.github.ynfeng.commander.raft;

import com.github.ynfeng.commander.support.Address;
import com.google.common.collect.Maps;
import java.util.Map;

public class Membership {
    private final Map<MemberId, MemberAddress> members = Maps.newHashMap();

    private Membership() {
    }

    public Membership addMember(MemberId id, Address memberAddress) {
        members.put(id, MemberAddress.create(id, memberAddress));
        return this;
    }

    public static Membership create() {
        return new Membership();
    }
}
