package com.github.ynfeng.commander.raft;

public class RaftMemberConfig {
    private long electionTimeoutDetectionInterval;

    private RaftMemberConfig() {

    }

    public long getElectionTimeoutDetectionInterval() {
        return electionTimeoutDetectionInterval;
    }

    public static RaftMemberConfigBuilder builder() {
        return new RaftMemberConfigBuilder();
    }

    public static class RaftMemberConfigBuilder {
        private final RaftMemberConfig config = new RaftMemberConfig();

        private RaftMemberConfigBuilder() {
        }

        public RaftMemberConfigBuilder electionTimeoutDetectionInterval(long ms) {
            config.electionTimeoutDetectionInterval = ms;
            return this;
        }

        public RaftMemberConfig build() {
            return config;
        }
    }
}
