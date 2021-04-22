package com.github.ynfeng.commander.raft;

import com.github.ynfeng.commander.support.ManageableSupport;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class RaftMember extends ManageableSupport {
    private volatile MemberRole role;
    private final MemberId id;
    private final ElectionTimeoutDetector electionTimeoutDetector;
    private final ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1);

    public RaftMember(MemberId id) {
        this.id = id;
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
        executor.scheduleWithFixedDelay(this::detectElectionTimeout, 0, 100, TimeUnit.MILLISECONDS);
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
}
