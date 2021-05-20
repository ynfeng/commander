package com.github.ynfeng.commander.raft.roles;

import com.github.ynfeng.commander.raft.protocol.LeaderHeartbeat;
import com.github.ynfeng.commander.raft.protocol.RequestVoteResponse;
import com.github.ynfeng.commander.raft.protocol.VoteRequest;

public interface RaftRole {
    void prepare();

    void destory();

    RequestVoteResponse handleRequestVote(VoteRequest voteRequest);

    void handleHeartBeat(LeaderHeartbeat heartbeat);
}
