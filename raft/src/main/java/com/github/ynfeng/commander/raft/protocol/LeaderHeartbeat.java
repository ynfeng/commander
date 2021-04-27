package com.github.ynfeng.commander.raft.protocol;

import com.github.ynfeng.commander.raft.MemberId;
import com.github.ynfeng.commander.raft.Term;

public class LeaderHeartbeat implements Request {
    private Term term;
    private MemberId leaderId;
    private long prevLogIndex;
    private Term prevLogTerm;
    private long leaderCommit;

    public Term term() {
        return term;
    }

    public MemberId leaderId() {
        return leaderId;
    }

    public long prevLogIndex() {
        return prevLogIndex;
    }

    public Term prevLogTerm() {
        return prevLogTerm;
    }

    public long leaderCommit() {
        return leaderCommit;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Term term;
        private MemberId leaderId;
        private long prevLogIndex;
        private Term prevLogTerm;
        private long leaderCommit;

        private Builder() {

        }

        public Builder term(Term term) {
            this.term = term;
            return this;
        }

        public Builder leaderId(MemberId leaderId) {
            this.leaderId = leaderId;
            return this;
        }

        public Builder prevLogIndex(long prevLogIndex) {
            this.prevLogIndex = prevLogIndex;
            return this;
        }

        public Builder prevLogTerm(Term prevLogTerm) {
            this.prevLogTerm = prevLogTerm;
            return this;
        }

        public Builder leaderCommit(long leaderCommit) {
            this.leaderCommit = leaderCommit;
            return this;
        }

        public LeaderHeartbeat build() {
            LeaderHeartbeat heartbeat = new LeaderHeartbeat();
            heartbeat.term = term;
            heartbeat.leaderId = leaderId;
            heartbeat.prevLogIndex = prevLogIndex;
            heartbeat.prevLogTerm = prevLogTerm;
            heartbeat.leaderCommit = leaderCommit;
            return heartbeat;
        }
    }
}
