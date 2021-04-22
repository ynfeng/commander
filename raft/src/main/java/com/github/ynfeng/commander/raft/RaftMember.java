package com.github.ynfeng.commander.raft;

import com.github.ynfeng.commander.support.ManageableSupport;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class RaftMember extends ManageableSupport {
    private volatile MemberRole role;
    private final MemberId id;
    private final RaftMemberConfig config;
    private final ElectionTimeoutDetector electionTimeoutDetector;
    private final ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1);

    private RaftMember(MemberId id, RaftMemberConfig config) {
        this.id = id;
        this.config = config;
        role = MemberRole.FLLOWER;
        electionTimeoutDetector = new ElectionTimeoutDetector();
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
        long electionTimeoutDetectionInterval = config.getElectionTimeoutDetectionInterval();
        executor.scheduleWithFixedDelay(this::detectElectionTimeout,
            electionTimeoutDetectionInterval,
            electionTimeoutDetectionInterval,
            TimeUnit.MILLISECONDS);
    }

    private void detectElectionTimeout() {
        if (electionTimeoutDetector.isTimeout()) {
            becomeCandidate();
        }
    }

    private void becomeCandidate() {
        role = MemberRole.CANDIDATE;
    }

    @Override
    protected void doShutdown() {
        executor.shutdownNow();
    }

    public static RaftMemberBuilder builder() {
        return new RaftMemberBuilder();
    }

    public static class RaftMemberBuilder {
        private MemberId memberId;
        private RaftMemberConfig config;

        private RaftMemberBuilder() {
        }

        public RaftMemberBuilder memberId(MemberId memberId) {
            this.memberId = memberId;
            return this;
        }

        public RaftMemberBuilder memberConfig(RaftMemberConfig config) {
            this.config = config;
            return this;
        }

        public RaftMember build() {
            return new RaftMember(memberId, config);
        }
    }
}
