package com.github.ynfeng.commander.raft.roles;

import com.github.ynfeng.commander.raft.RaftContext;
import com.github.ynfeng.commander.raft.RemoteMember;
import com.github.ynfeng.commander.raft.protocol.LeaderHeartbeat;
import com.github.ynfeng.commander.raft.protocol.RequestVoteResponse;
import com.github.ynfeng.commander.raft.protocol.VoteRequest;
import com.github.ynfeng.commander.support.logger.CmderLoggerFactory;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;

public class Leader extends AbstratRaftRole {
    private static final Logger LOGGER = CmderLoggerFactory.getSystemLogger();
    private final RaftContext raftContext;
    private final long heartbeatInterval;
    private final ScheduledThreadPoolExecutor heartbeatExecutor = new ScheduledThreadPoolExecutor(1,
        new ThreadFactoryBuilder().setNameFormat("heartbeat-thread-%d").build());

    public Leader(RaftContext raftContext, long heartbeatInterval) {
        super(raftContext);
        this.raftContext = raftContext;
        this.heartbeatInterval = heartbeatInterval;
    }

    private void heartbeat() {
        raftContext.remoteMembers().forEach(this::sendHeartbeatToFollower);
    }

    private void sendHeartbeatToFollower(RemoteMember remoteMember) {
        LeaderHeartbeat heartbeat = createHeartbeat();
        raftContext().sendRequest(remoteMember, heartbeat);
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
        raftContext().pauseElectionTimer();
        heartbeatExecutor.scheduleWithFixedDelay(this::heartbeat,
            heartbeatInterval,
            heartbeatInterval,
            TimeUnit.MILLISECONDS);
    }

    @Override
    public RequestVoteResponse handleRequestVote(VoteRequest voteRequest) {
        if (voteRequest.term().greaterThan(raftContext().currentTerm())) {
            raftContext.becomeCandidate();
        }
        return RequestVoteResponse.declined(raftContext.currentTerm(), raftContext.localMermberId());
    }

    @Override
    public void destory() {
        heartbeatExecutor.shutdownNow();
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("leader {} has shutdown.", raftContext().localMermberId().id());
        }
    }

    @Override
    public void handleHeartBeat(LeaderHeartbeat heartbeat) {
        if (heartbeat.term().greaterThan(raftContext().currentTerm())) {
            raftContext.becomeFollower(heartbeat.term(), heartbeat.leaderId());
        }
    }
}
