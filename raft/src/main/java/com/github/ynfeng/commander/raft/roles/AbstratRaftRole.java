package com.github.ynfeng.commander.raft.roles;

import com.github.ynfeng.commander.raft.RaftContext;

public abstract class AbstratRaftRole implements RaftRole {
    private final RaftContext raftContext;

    protected AbstratRaftRole(RaftContext raftContext) {
        this.raftContext = raftContext;
    }

    protected RaftContext raftContext() {
        return raftContext;
    }
}
