package com.github.ynfeng.commander.raft.roles;

public class Follower implements RaftRole {
    @Override
    public void prepare() {
        //do nothing
    }

    @Override
    public void requestVote() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void destory() {
        //do nothing
    }
}
