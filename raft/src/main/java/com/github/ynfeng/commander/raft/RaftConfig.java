package com.github.ynfeng.commander.raft;

public class RaftConfig {
    private long electionTimeout = 500;
    private long leaderHeartbeatInterval = 150;
    private int serverThreadPoolSize = Runtime.getRuntime().availableProcessors() * 2;

    private RaftConfig() {
    }

    public long electionTimeout() {
        return electionTimeout;
    }

    public int threadPoolSize() {
        return serverThreadPoolSize;
    }

    public long leaderHeartbeatInterval() {
        return leaderHeartbeatInterval;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private long timeoutMs;
        private int serverThreadPoolSize;
        private long leaderHeartbeatInterval;

        public Builder electionTimeout(long timeoutMs) {
            this.timeoutMs = timeoutMs;
            return this;
        }

        public Builder serverThreadPoolSize(int serverThreadPoolSize) {
            this.serverThreadPoolSize = serverThreadPoolSize;
            return this;
        }

        public Builder leaderHeartbeatInterval(long leaderHeartbeatInterval) {
            this.leaderHeartbeatInterval = leaderHeartbeatInterval;
            return this;
        }

        public RaftConfig build() {
            RaftConfig config = new RaftConfig();
            config.electionTimeout = timeoutMs;
            config.serverThreadPoolSize = serverThreadPoolSize;
            config.leaderHeartbeatInterval = leaderHeartbeatInterval;
            return config;
        }
    }
}
