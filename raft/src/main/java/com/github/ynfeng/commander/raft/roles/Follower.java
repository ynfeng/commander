package com.github.ynfeng.commander.raft.roles;

import com.github.ynfeng.commander.raft.RaftContext;
import com.github.ynfeng.commander.raft.VoteTracker;
import com.github.ynfeng.commander.raft.protocol.LeaderHeartbeat;
import com.github.ynfeng.commander.raft.protocol.RequestVoteResponse;
import com.github.ynfeng.commander.raft.protocol.VoteRequest;

public class Follower extends AbstratRaftRole {

    public Follower(RaftContext raftContext) {
        super(raftContext);
        new VoteTracker().reset();
    }

    @Override
    public void prepare() {
        raftContext().resumeElectionTimer();
        raftContext().resetElectionTimer();
    }

    @Override
    public void destory() {
        //do nothing
    }

    @Override
    public synchronized RequestVoteResponse handleRequestVote(VoteRequest voteRequest) {
        if (voteRequest.term().greaterThan(raftContext().currentTerm())) {
            raftContext().becomeCandidate();
            raftContext().tryUpdateCurrentTerm(voteRequest.term());
        }
        return RequestVoteResponse.declined(raftContext().currentTerm(), raftContext().localMermberId());
    }

    @Override
    public void handleHeartBeat(LeaderHeartbeat heartbeat) {
        if (heartbeat.term().equals(raftContext().currentTerm())
            && heartbeat.leaderId().equals(raftContext().currentLeader())) {
            raftContext().resetElectionTimer();
        }
    }
}
