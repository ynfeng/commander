package com.github.ynfeng.commander.raft;

import com.github.ynfeng.commander.support.ManageableSupport;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class RaftMember extends ManageableSupport {
    private volatile MemberRole role;
    private final MemberId id;
    private final LocalConfig config;
    private final Membership membership;
    private final ElectionTimeoutDetector electionTimeoutDetector;
    private final ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1);

    private RaftMember(MemberId id, LocalConfig config, Membership membership) {
        this.id = id;
        this.config = config;
        this.membership = membership;
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
        connectToEachMember();
        startElectionTimeoutDetect();
    }

    private void connectToEachMember() {
        List<MemberAddress> otherMemberAddresss = membership.otherMemberAddresses(id);

    }

    private void startElectionTimeoutDetect() {
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
        private LocalConfig config;
        private Membership membership;

        private RaftMemberBuilder() {
        }

        public RaftMemberBuilder memberId(MemberId memberId) {
            this.memberId = memberId;
            return this;
        }

        public RaftMemberBuilder membership(Membership membership) {
            this.membership = membership;
            return this;
        }

        public RaftMemberBuilder localConfig(LocalConfig config) {
            this.config = config;
            return this;
        }

        public RaftMember build() {
            return new RaftMember(memberId, config, membership);
        }
    }
}
