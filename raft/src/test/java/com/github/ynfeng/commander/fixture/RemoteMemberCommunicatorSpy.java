package com.github.ynfeng.commander.fixture;

import com.github.ynfeng.commander.raft.MemberId;
import com.github.ynfeng.commander.raft.Term;
import com.github.ynfeng.commander.raft.protocol.LeaderHeartbeat;
import com.github.ynfeng.commander.raft.protocol.Request;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class RemoteMemberCommunicatorSpy {
    private final AtomicInteger leaderHeartbeatTimes = new AtomicInteger();
    private final AtomicReference<LeaderHeartbeatRecord> lastLeaderHeartBeatHolder = new AtomicReference<>(new LeaderHeartbeatRecord(Term.create(0), MemberId.create("none")));
    private final Object watitLock = new Object();

    public void reset() {
        leaderHeartbeatTimes.set(0);
        lastLeaderHeartBeatHolder.set(null);
    }

    public void receiveRequest(Request request) {
        LeaderHeartbeatRecord lastLeaderHeartbeat = lastLeaderHeartBeatHolder.get();
        if (request instanceof LeaderHeartbeat) {
            LeaderHeartbeat heartbeat = (LeaderHeartbeat) request;
            if (lastLeaderHeartbeat.isSame(heartbeat.term(), heartbeat.leaderId())) {
                int times = leaderHeartbeatTimes.incrementAndGet();
                if (times >= 5) {
                    synchronized (watitLock) {
                        watitLock.notifyAll();
                    }
                }
            } else {
                leaderHeartbeatTimes.set(0);
            }
            lastLeaderHeartBeatHolder.set(new LeaderHeartbeatRecord(heartbeat.term(), heartbeat.leaderId()));
        }
    }

    public MemberId expectLeader() throws InterruptedException {
        if (leaderHeartbeatTimes.get() >= 5) {
            return lastLeaderHeartBeatHolder.get().leaderId;
        }

        synchronized (watitLock) {
            watitLock.wait();
        }

        return lastLeaderHeartBeatHolder.get().leaderId;
    }
}
