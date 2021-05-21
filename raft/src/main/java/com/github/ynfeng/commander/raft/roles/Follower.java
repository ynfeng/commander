package com.github.ynfeng.commander.raft.roles;

import com.github.ynfeng.commander.raft.RaftContext;
import com.github.ynfeng.commander.raft.protocol.LeaderHeartbeat;
import com.github.ynfeng.commander.raft.protocol.RequestVoteResponse;
import com.github.ynfeng.commander.raft.protocol.VoteRequest;

public class Follower extends AbstratRaftRole {

    public Follower(RaftContext raftContext) {
        super(raftContext);
    }

    @Override
    public void prepare() {
        raftContext().resumeElectionTimer();
    }

    @Override
    public void destory() {
        //do nothing
    }

    @Override
    public RequestVoteResponse handleRequestVote(VoteRequest voteRequest) {
        return null;
    }

    @Override
    public void handleHeartBeat(LeaderHeartbeat heartbeat) {
        if (heartbeat.term().greaterOrEqual(raftContext().currentTerm())) {
            raftContext().resetElectionTimer();
            raftContext().setLeader(heartbeat.leaderId());
        }
    }
}
