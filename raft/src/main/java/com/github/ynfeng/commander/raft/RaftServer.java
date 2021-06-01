package com.github.ynfeng.commander.raft;

import com.github.ynfeng.commander.raft.protocol.EmptyResponse;
import com.github.ynfeng.commander.raft.protocol.LeaderHeartbeat;
import com.github.ynfeng.commander.raft.protocol.Request;
import com.github.ynfeng.commander.raft.protocol.RequestVoteResponse;
import com.github.ynfeng.commander.raft.protocol.Response;
import com.github.ynfeng.commander.raft.protocol.VoteRequest;
import com.github.ynfeng.commander.raft.roles.Candidate;
import com.github.ynfeng.commander.raft.roles.Follower;
import com.github.ynfeng.commander.raft.roles.Leader;
import com.github.ynfeng.commander.raft.roles.RaftRole;
import com.github.ynfeng.commander.support.Address;
import com.github.ynfeng.commander.support.ManageableSupport;
import com.github.ynfeng.commander.support.logger.CmderLoggerFactory;
import com.google.common.base.Preconditions;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.slf4j.Logger;

public class RaftServer extends ManageableSupport implements RaftMember, RaftContext {
    private static final Logger LOGGER = CmderLoggerFactory.getSystemLogger();
    private final RaftConfig raftConfig;
    private Address localAddress;
    private RaftRole role;
    private MemberId leader;
    private MemberId localMemberId;
    private RaftGroup raftGroup;
    private final ElectionTimer electionTimer;
    private RaftMemberDiscovery raftMemberDiscovery;
    private RemoteMemberCommunicator remoteMemberCommunicator;
    private Term currentTerm;
    private final VoteTracker voteTracker = new VoteTracker();
    private final ExecutorService serverExecutor;

    private RaftServer(RaftConfig raftConfig) {
        this.raftConfig = raftConfig;
        currentTerm = Term.create(0);
        electionTimer = new ElectionTimer(raftConfig.electionTimeout(), this::becomeCandidate);
        serverExecutor = Executors.newSingleThreadExecutor(new ThreadFactoryBuilder()
            .setUncaughtExceptionHandler((t, e) -> LOGGER.error("raft server error.", e))
            .setNameFormat("raft-server-thread-%d")
            .build());
        role = new Follower(this);
    }

    @Override
    public void becomeCandidate() {
        serverExecutor.execute(() -> {
            electionTimer.reset();
            voteTracker().reset();
            changeRole(new Candidate(this));
        });
    }

    private void changeRole(RaftRole newRole) {
        if (role != null) {
            role.destory();
        }
        role = newRole;
        role.prepare();
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    protected void doStart() {
        remoteMemberCommunicator.registerHandler(VoteRequest.class, this::handleRequestVote);
        remoteMemberCommunicator.registerHandler(LeaderHeartbeat.class, this::handleLeaderHeartbeat);
        raftMemberDiscovery.start();
        electionTimer.start();
        LOGGER.info("{} started.", localMemberId.id());
    }

    private EmptyResponse handleLeaderHeartbeat(LeaderHeartbeat heartbeat) {
        try {
            return serverExecutor.submit(() -> {
                role.handleHeartBeat(heartbeat);
                return new EmptyResponse();
            }).get();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            LOGGER.warn("raft server thread has interrupted");
            throw new RaftServerException(e);
        } catch (ExecutionException e) {
            throw new RaftServerException(e);
        }
    }

    private RequestVoteResponse handleRequestVote(VoteRequest voteRequest) {
        try {
            return serverExecutor.submit(
                () -> role.handleRequestVote(voteRequest)).get();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            LOGGER.warn("raft server thread has interrupted");
            throw new RaftServerException(e);
        } catch (ExecutionException e) {
            throw new RaftServerException(e);
        }
    }

    @Override
    protected void doShutdown() {
        electionTimer.shutdown();
        raftMemberDiscovery.shutdown();
        role.destory();
    }

    @Override
    public MemberId id() {
        return localMemberId;
    }

    @Override
    public MemberId localMermberId() {
        return localMemberId;
    }

    @Override
    public List<RemoteMember> remoteMembers() {
        return raftMemberDiscovery.remoteMembers(localMemberId);
    }

    @Override
    public Term lastLogTerm() {
        return Term.create(0);
    }

    @Override
    public long lastLogIndex() {
        return 0;
    }

    @Override
    public Term currentTerm() {
        return currentTerm;
    }

    @Override
    public int quorum() {
        return raftGroup.quorum();
    }

    @Override
    public void becomeLeader() {
        serverExecutor.execute(() -> {
            LOGGER.info("{} become leader at term {}.", localMemberId.id(), currentTerm().value());
            changeRole(new Leader(this, raftConfig.leaderHeartbeatInterval()));
        });
    }

    @Override
    public long lastCommitIndex() {
        return 0;
    }

    @Override
    public long prevLogIndex() {
        return 0;
    }

    @Override
    public Term prevLogTerm() {
        return Term.create(0);
    }

    @Override
    public void resetElectionTimer() {
        electionTimer.reset();
    }

    @Override
    public void becomeFollower(Term term, MemberId leaderId) {
        serverExecutor.execute(() -> {
            LOGGER.info("{} become follower at term {} current leader is {}.",
                localMemberId.id(), term.value(), leaderId.id());
            leader = leaderId;
            currentTerm = term;
            changeRole(new Follower(this));
        });
    }

    @Override
    public void pauseElectionTimer() {
        electionTimer.pause();
    }

    @Override
    public void resumeElectionTimer() {
        electionTimer.resume();
    }

    @Override
    public VoteTracker voteTracker() {
        return voteTracker;
    }

    @Override
    public void nextTerm() {
        currentTerm = currentTerm.nextTerm();
    }

    @Override
    public MemberId currentLeader() {
        return leader;
    }

    @Override
    public boolean isLeader() {
        if (role == null) {
            return false;
        }
        return role instanceof Leader;
    }

    @Override
    public <R extends Response> CompletableFuture<R> sendRequest(RemoteMember remoteMember, Request request) {
        return remoteMemberCommunicator.send(remoteMember, request);
    }

    public static class Builder {
        private Address localAddress;
        private MemberId localMemberId;
        private RaftConfig raftConfig;
        private RaftGroup raftGroup;
        private RaftMemberDiscovery raftMemberDiscovery;
        private RemoteMemberCommunicator remoteMemberCommunicator;

        private Builder() {
        }

        @SuppressWarnings("checkstyle:MethodLength")
        public RaftServer build() {
            Preconditions.checkNotNull(raftConfig, "raft config not set.");
            RaftServer raftServer = new RaftServer(raftConfig);

            Preconditions.checkNotNull(localAddress, "local address not set.");
            raftServer.localAddress = localAddress;

            Preconditions.checkNotNull(localMemberId, "local member id not set.");
            raftServer.localMemberId = localMemberId;

            Preconditions.checkNotNull(raftGroup, "raft group not set.");
            raftServer.raftGroup = raftGroup;

            Preconditions.checkNotNull(raftMemberDiscovery, "raft member discover not set.");
            raftServer.raftMemberDiscovery = raftMemberDiscovery;

            Preconditions.checkNotNull(remoteMemberCommunicator, "remote member communicator discover not set.");
            raftServer.remoteMemberCommunicator = remoteMemberCommunicator;
            return raftServer;
        }

        public Builder localAddress(Address localAddress) {
            this.localAddress = localAddress;
            return this;
        }

        public Builder localMemberId(MemberId localMemberId) {
            this.localMemberId = localMemberId;
            return this;
        }

        public Builder raftConfig(RaftConfig raftConfig) {
            this.raftConfig = raftConfig;
            return this;
        }

        public Builder raftGroup(RaftGroup raftGroup) {
            this.raftGroup = raftGroup;
            return this;
        }

        public Builder raftMemberDiscovery(RaftMemberDiscovery raftMemberDiscovery) {
            this.raftMemberDiscovery = raftMemberDiscovery;
            return this;
        }

        public Builder remoteMemberCommunicator(RemoteMemberCommunicator remoteMemberCommunicator) {
            this.remoteMemberCommunicator = remoteMemberCommunicator;
            return this;
        }
    }
}
