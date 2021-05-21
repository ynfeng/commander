package com.github.ynfeng.commander.raft.roles;

import com.github.ynfeng.commander.raft.RaftContext;
import com.github.ynfeng.commander.raft.RemoteMember;
import com.github.ynfeng.commander.raft.RemoteMemberCommunicator;
import com.github.ynfeng.commander.raft.VoteTracker;
import com.github.ynfeng.commander.raft.protocol.LeaderHeartbeat;
import com.github.ynfeng.commander.raft.protocol.RequestVoteResponse;
import com.github.ynfeng.commander.raft.protocol.VoteRequest;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Leader extends AbstratRaftRole {
    private final RaftContext raftContext;
    private final long heartbeatInterval;
    private final VoteTracker voteTracker;
    private final ScheduledThreadPoolExecutor heartbeatExecutor = new ScheduledThreadPoolExecutor(1,
        new ThreadFactoryBuilder().setNameFormat("heartbeat-thread-%d").build());

    public Leader(RaftContext raftContext, long heartbeatInterval) {
        super(raftContext);
        this.raftContext = raftContext;
        this.heartbeatInterval = heartbeatInterval;
        voteTracker = raftContext().voteTracker();
        raftContext.voteTracker().resetAll();
    }

    private void heartbeat() {
        raftContext.pauseElectionTimer();
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
        heartbeatExecutor.scheduleWithFixedDelay(this::heartbeat,
            heartbeatInterval,
            heartbeatInterval,
            TimeUnit.MILLISECONDS);
    }

    @Override
    public synchronized RequestVoteResponse handleRequestVote(VoteRequest voteRequest) {
        if (voteRequest.term().greaterThan(raftContext().currentTerm())) {
            voteTracker.recordVoteCast(voteRequest.term(), voteRequest.candidateId());
            raftContext.tryUpdateCurrentTerm(voteRequest.term());
            raftContext.becomeCandidate();
            return RequestVoteResponse.voted(raftContext.currentTerm(), raftContext.localMermberId());
        }
        return RequestVoteResponse.declined(raftContext.currentTerm(), raftContext.localMermberId());
    }

    @Override
    public void destory() {
        heartbeatExecutor.shutdownNow();
    }

    @Override
    public void handleHeartBeat(LeaderHeartbeat heartbeat) {
        if (heartbeat.term().greaterThan(raftContext().currentTerm())) {
            raftContext().becomeFollower(heartbeat.leaderId());
        }
    }
}
