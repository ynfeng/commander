package com.github.ynfeng.commander.raft;

public class RaftConfig {
    private long electionTimeoutDetectionInterval;

    private RaftConfig() {
    }

    public long electionTimeoutDetectionInterval() {
        return electionTimeoutDetectionInterval;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private long intervalMs;

        public Builder electionTimeoutDetectionInterval(long intervalMs) {
            this.intervalMs = intervalMs;
            return this;
        }

        public RaftConfig build() {
            RaftConfig config = new RaftConfig();
            config.electionTimeoutDetectionInterval = intervalMs;
            return config;
        }
    }
}
