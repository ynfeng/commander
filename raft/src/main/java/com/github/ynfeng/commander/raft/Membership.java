package com.github.ynfeng.commander.raft;

import com.github.ynfeng.commander.support.Address;
import com.google.common.collect.Maps;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    public List<MemberAddress> otherMemberAddresses(MemberId memberId) {
        return members.entrySet().stream()
            .filter(eachEntry -> !eachEntry.getKey().equals(memberId))
            .map(Map.Entry::getValue)
            .collect(Collectors.toList());
    }
}
