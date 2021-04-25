package com.github.ynfeng.commander.raft;

import com.github.ynfeng.commander.support.ManageableSupport;

public class RaftMember extends ManageableSupport {
    private volatile MemberRole role;
    private final MemberId id;
    private final LocalConfig config;
    private final MemberIds memberIds;
    private final ElectionTimeoutDetector electionTimeoutDetector;

    private RaftMember(MemberId id, LocalConfig config, MemberIds memberIds) {
        this.id = id;
        this.config = config;
        this.memberIds = memberIds;
        role = MemberRole.FLLOWER;
        electionTimeoutDetector
            = new ElectionTimeoutDetector(config.getElectionTimeoutDetectionInterval(), this::electionTimeout);
    }

    public boolean isFollower() {
        return role == MemberRole.FLLOWER;
    }

    public MemberId id() {
        return id;
    }

    public boolean isCandicate() {
        return role == MemberRole.CANDIDATE;
    }

    @Override
    protected void doStart() {
        electionTimeoutDetector.start();
    }

    private void electionTimeout() {
        role = MemberRole.CANDIDATE;
    }

    @Override
    protected void doShutdown() {
        electionTimeoutDetector.shutdown();
    }

    public static RaftMemberBuilder builder() {
        return new RaftMemberBuilder();
    }

    public static class RaftMemberBuilder {
        private MemberId memberId;
        private LocalConfig config;
        private MemberIds memberIds;

        private RaftMemberBuilder() {
        }

        public RaftMemberBuilder memberId(MemberId memberId) {
            this.memberId = memberId;
            return this;
        }

        public RaftMemberBuilder memberIds(MemberIds memberIds) {
            this.memberIds = memberIds;
            return this;
        }

        public RaftMemberBuilder localConfig(LocalConfig config) {
            this.config = config;
            return this;
        }

        public RaftMember build() {
            return new RaftMember(memberId, config, memberIds);
        }
    }
}
