package com.github.ynfeng.commander.raft.roles;

public interface RaftRole {
    void prepare();

    void requestVote();

    void destory();
}
