package com.github.ynfeng.commander.raft;

public class LocalConfig {
    private long electionTimeoutDetectionInterval;

    private LocalConfig() {

    }

    public long getElectionTimeoutDetectionInterval() {
        return electionTimeoutDetectionInterval;
    }

    public static LocalConfigBUilder builder() {
        return new LocalConfigBUilder();
    }

    public static class LocalConfigBUilder {
        private final LocalConfig config = new LocalConfig();

        private LocalConfigBUilder() {
        }

        public LocalConfigBUilder electionTimeoutDetectionInterval(long ms) {
            config.electionTimeoutDetectionInterval = ms;
            return this;
        }

        public LocalConfig build() {
            return config;
        }
    }
}
