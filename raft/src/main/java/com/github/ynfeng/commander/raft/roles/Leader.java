package com.github.ynfeng.commander.raft.roles;

import com.github.ynfeng.commander.raft.RaftContext;
import com.github.ynfeng.commander.raft.RemoteMember;
import com.github.ynfeng.commander.raft.RemoteMemberCommunicator;
import com.github.ynfeng.commander.raft.protocol.LeaderHeartbeat;
import com.github.ynfeng.commander.raft.protocol.RequestVote;
import com.github.ynfeng.commander.raft.protocol.RequestVoteResponse;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Leader implements RaftRole {
    private final RaftContext raftContext;
    private final long heartbeatInterval;
    private final ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1);

    public Leader(RaftContext raftContext, long heartbeatInterval) {
        this.raftContext = raftContext;
        this.heartbeatInterval = heartbeatInterval;
    }

    private void heartbeat() {
        raftContext.resetElectionTimer();
        raftContext.remoteMembers().forEach(this::sendHeartbeatToFollower);
    }

    private void sendHeartbeatToFollower(RemoteMember remoteMember) {
        RemoteMemberCommunicator communicator = raftContext.remoteMemberCommunicator();
        LeaderHeartbeat heartbeat = createHeartbeat();
        communicator.send(remoteMember, heartbeat);
    }

    private LeaderHeartbeat createHeartbeat() {
        return LeaderHeartbeat.builder()
            .leaderId(raftContext.localMermberId())
            .term(raftContext.currentTerm())
            .leaderCommit(raftContext.lastCommitIndex())
            .prevLogIndex(raftContext.prevLogIndex())
            .prevLogTerm(raftContext.prevLogTerm())
            .build();
    }

    @Override
    public void prepare() {
        executor.scheduleWithFixedDelay(this::heartbeat,
            heartbeatInterval,
            heartbeatInterval,
            TimeUnit.MILLISECONDS);
    }

    @Override
    public void destory() {
        executor.shutdownNow();
    }

    @Override
    public RequestVoteResponse handleRequestVote(RequestVote requestVote) {
        return null;
    }
}
