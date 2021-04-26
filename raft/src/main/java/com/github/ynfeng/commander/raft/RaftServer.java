package com.github.ynfeng.commander.raft;

import com.github.ynfeng.commander.support.Address;
import com.github.ynfeng.commander.support.ManageableSupport;
import com.google.common.base.Preconditions;

public class RaftServer extends ManageableSupport {
    private Address localAddress;
    private volatile RaftRole role;
    private MemberId localMemberId;
    private RaftConfig raftConfig;
    private final ElectionTimeoutDetector electionTimeoutDetector;

    private RaftServer(RaftConfig raftConfig) {
        role = new Follower();
        this.raftConfig = raftConfig;
        electionTimeoutDetector
            = new ElectionTimeoutDetector(raftConfig.electionTimeoutDetectionInterval(), this::electionTimeout);
    }

    private void electionTimeout() {
        becameCandidate();
    }

    private void becameCandidate() {
        role = new Candidate();
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    protected void doStart() {
        electionTimeoutDetector.start();
    }

    @Override
    protected void doShutdown() {
        electionTimeoutDetector.shutdown();
    }

    public static class Builder {
        private Address localAddress;
        private MemberId localMemberId;
        private RaftConfig raftConfig;

        private Builder() {
        }

        public RaftServer build() {
            Preconditions.checkNotNull(raftConfig, "raft config not set.");
            RaftServer raftServer = new RaftServer(raftConfig);
            raftServer.localAddress = localAddress;
            raftServer.localMemberId = localMemberId;
            raftServer.raftConfig = raftConfig;
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
    }
}
