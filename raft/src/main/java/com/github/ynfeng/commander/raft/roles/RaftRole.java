package com.github.ynfeng.commander.raft.roles;

import com.github.ynfeng.commander.raft.protocol.RequestVote;
import com.github.ynfeng.commander.raft.protocol.RequestVoteResponse;

public interface RaftRole {
    void prepare();

    void destory();

    RequestVoteResponse handleRequestVote(RequestVote requestVote);
}
