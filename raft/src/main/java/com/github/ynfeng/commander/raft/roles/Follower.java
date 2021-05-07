package com.github.ynfeng.commander.raft.roles;

import com.github.ynfeng.commander.raft.RaftContext;
import com.github.ynfeng.commander.raft.protocol.RequestVote;
import com.github.ynfeng.commander.raft.protocol.RequestVoteResponse;

public class Follower implements RaftRole {
    private final RaftContext raftContext;

    public Follower(RaftContext raftContext) {
        this.raftContext = raftContext;
    }

    @Override
    public void prepare() {
        //do nothing
    }

    @Override
    public void destory() {
        //do nothing
    }

    @Override
    public RequestVoteResponse handleRequestVote(RequestVote requestVote) {
        return RequestVoteResponse.voted(raftContext.currentTerm(), raftContext.localMermberId());
    }
}
