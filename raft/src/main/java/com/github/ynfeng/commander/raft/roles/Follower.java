package com.github.ynfeng.commander.raft.roles;

import com.github.ynfeng.commander.raft.RaftContext;

public class Follower extends AbstratRaftRole {

    public Follower(RaftContext raftContext) {
        super(raftContext);
    }

    @Override
    public void prepare() {
        //do nothing
    }

    @Override
    public void destory() {
        //do nothing
    }
}
