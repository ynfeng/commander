package com.github.ynfeng.commander.raft;

import com.github.ynfeng.commander.support.Address;

public class RemoteMember {
    private final MemberId memberId;
    private final Address address;

    private RemoteMember(MemberId memberId, Address address) {
        this.memberId = memberId;
        this.address = address;
    }

    public static RemoteMember create(MemberId memberId, Address address) {
        return new RemoteMember(memberId, address);
    }

    public MemberId id() {
        return memberId;
    }

    @Override
    public String toString() {
        return "RemoteMember{" +
            "memberId=" + memberId +
            ", address=" + address +
            '}';
    }

    public Address address() {
        return address;
    }
}
