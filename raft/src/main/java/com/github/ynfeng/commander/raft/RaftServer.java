package com.github.ynfeng.commander.raft;

import com.github.ynfeng.commander.raft.protocol.EmptyResponse;
import com.github.ynfeng.commander.raft.protocol.LeaderHeartbeat;
import com.github.ynfeng.commander.raft.protocol.RequestVoteResponse;
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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;
import org.slf4j.Logger;

public class RaftServer extends ManageableSupport implements RaftMember, RaftContext {
    private static final Logger LOGGER = CmderLoggerFactory.getSystemLogger();
    private final RaftConfig raftConfig;
    private Address localAddress;
    private final AtomicReference<RaftRole> role = new AtomicReference<>();
    private final AtomicReference<MemberId> leader = new AtomicReference<>();
    private MemberId localMemberId;
    private RaftGroup raftGroup;
    private final ElectionTimer electionTimer;
    private RaftMemberDiscovery raftMemberDiscovery;
    private RemoteMemberCommunicator remoteMemberCommunicator;
    private final AtomicReference<Term> currentTerm = new AtomicReference<>();
    private final ExecutorService serverThreadPool;
    private final VoteTracker voteTracker = new VoteTracker();

    private RaftServer(RaftConfig raftConfig) {
        this.raftConfig = raftConfig;
        currentTerm.set(Term.create(0));
        serverThreadPool = Executors.newFixedThreadPool(raftConfig.threadPoolSize(),
            new ThreadFactoryBuilder().setNameFormat("raft-server-thread-%d").build());
        electionTimer = new ElectionTimer(raftConfig.electionTimeout(), this::electionTimeout);
    }

    private void electionTimeout() {
        serverThreadPool.submit(() -> {
            electionTimer.reset();
            becomeCandidate();
        });
    }

    @Override
    public void becomeCandidate() {
        LOGGER.info("{} become candidate.", localMemberId.id());
        changeRole(new Candidate(this));
    }

    private void changeRole(RaftRole role) {
        if (this.role.get() != null) {
            this.role.get().destory();
        }
        this.role.set(role);
        this.role.get().prepare();
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
        role.get().handleHeartBeat(heartbeat);
        return new EmptyResponse();
    }

    private RequestVoteResponse handleRequestVote(VoteRequest voteRequest) {
        return role.get().handleRequestVote(voteRequest);
    }

    @Override
    protected void doShutdown() {
        electionTimer.shutdown();
        raftMemberDiscovery.shutdown();
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
    public RemoteMemberCommunicator remoteMemberCommunicator() {
        return remoteMemberCommunicator;
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
        return currentTerm.get();
    }

    @Override
    public void tryUpdateCurrentTerm(Term term) {
        if (term.greaterThan(currentTerm.get())) {
            currentTerm.set(term);
        }
    }

    @Override
    public int quorum() {
        return raftGroup.quorum();
    }

    @Override
    public void becomeLeader() {
        serverThreadPool.submit(() -> {
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
    public void becomeFollower(MemberId leaderId) {
        serverThreadPool.submit(() -> {
            LOGGER.info("{} become follower current leader is {}.", localMemberId.id(), leaderId.id());
            leader.set(leaderId);
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
    public void setLeader(MemberId leaderId) {
        leader.set(leaderId);
    }

    @Override
    public VoteTracker voteTracker() {
        return voteTracker;
    }

    @Override
    public void nextTerm() {
        currentTerm.set(currentTerm.get().nextTerm());
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
