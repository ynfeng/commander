package com.github.ynfeng.commander.raft;

import com.github.ynfeng.commander.raft.roles.Candidate;
import com.github.ynfeng.commander.raft.roles.Follower;
import com.github.ynfeng.commander.raft.roles.Leader;
import com.github.ynfeng.commander.raft.roles.RaftRole;
import com.github.ynfeng.commander.support.Address;
import com.github.ynfeng.commander.support.ManageableSupport;
import com.google.common.base.Preconditions;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;

public class RaftServer extends ManageableSupport implements RaftMember, RaftContext {
    private final RaftConfig raftConfig;
    private Address localAddress;
    private volatile RaftRole role;
    private MemberId localMemberId;
    private RaftGroup raftGroup;
    private final ElectionTimer electionTimer;
    private RaftMemberDiscovery raftMemberDiscovery;
    private RemoteMemberCommunicator remoteMemberCommunicator;
    private final AtomicReference<Term> currentTerm = new AtomicReference<>();
    private final ExecutorService serverThreadPool;

    private RaftServer(RaftConfig raftConfig) {
        this.raftConfig = raftConfig;
        currentTerm.set(Term.create(0));
        role = new Follower();
        electionTimer = new ElectionTimer(raftConfig.electionTimeout(), this::electionTimeout);
        serverThreadPool = Executors.newFixedThreadPool(raftConfig.threadPoolSize());
    }

    private void electionTimeout() {
        serverThreadPool.submit(() -> {
            electionTimer.reset();
            currentTerm.set(currentTerm.get().nextTerm());
            becomeCandidate();
        });
    }

    private void becomeCandidate() {
        changeRole(new Candidate(this));
        role.requestVote();
    }

    private void changeRole(RaftRole role) {
        this.role.destory();
        this.role = role;
        this.role.prepare();
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    protected void doStart() {
        raftMemberDiscovery.start();
        electionTimer.start();
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
        return raftMemberDiscovery.remoteMembers();
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
