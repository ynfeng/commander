package com.github.ynfeng.commander.raft;

import com.github.ynfeng.commander.support.ManageableSupport;

public class RaftMember extends ManageableSupport {
    private volatile MemberRole role;
    private MemberId id;
    private LocalConfig config;
    private MemberIds memberIds;
    private ElectionTimeoutDetector electionTimeoutDetector;

    private RaftMember() {
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
        becomeCandidate();
    }

    private void becomeCandidate() {
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
            RaftMember raftMember = new RaftMember();
            raftMember.id = memberId;
            raftMember.config = config;
            raftMember.memberIds = memberIds;
            return raftMember;
        }
    }
}
