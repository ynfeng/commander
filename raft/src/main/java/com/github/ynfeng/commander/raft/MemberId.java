package com.github.ynfeng.commander.raft;

public class MemberId {
    private final String id;

    private MemberId(String id) {
        this.id = id;
    }

    public static MemberId create(String id) {
        return new MemberId(id);
    }
}
