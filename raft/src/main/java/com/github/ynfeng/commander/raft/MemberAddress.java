package com.github.ynfeng.commander.raft;

import com.github.ynfeng.commander.support.Address;

public class MemberAddress {
    private final MemberId id;
    private final Address address;

    public MemberAddress(MemberId id, Address address) {
        this.id = id;
        this.address = address;
    }

    public static MemberAddress create(MemberId id, Address address) {
        return new MemberAddress(id, address);
    }
}
