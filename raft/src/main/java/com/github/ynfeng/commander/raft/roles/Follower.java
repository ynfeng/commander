package com.github.ynfeng.commander.raft.roles;

import com.github.ynfeng.commander.raft.RaftContext;
import com.github.ynfeng.commander.raft.protocol.LeaderHeartbeat;

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
    public void handleHeartBeat(LeaderHeartbeat heartbeat) {
        if (heartbeat.isLegal(raftContext())) {
            raftContext().resetElectionTimer();
            raftContext().setLeader(heartbeat.leaderId());
        }
    }
}
