package com.github.ynfeng.commander.cluster.discovery;

import com.github.ynfeng.commander.cluster.ClusterMember;
import com.github.ynfeng.commander.support.Config;

public class ClusterMembershipConfig implements Config {
    private ClusterMemberDiscoveryProtocolConfig memberDiscoveryProtocolConfig;
    private ClusterMember localMember;

    public static Builder builder() {
        return new Builder();
    }

    public ClusterMemberDiscoveryProtocolConfig memberDiscoveryProtocolConfig() {
        return memberDiscoveryProtocolConfig;
    }

    public ClusterMember localMember() {
        return localMember;
    }

    public static class Builder {
        private final ClusterMembershipConfig config = new ClusterMembershipConfig();

        public Builder memberDiscoveryConfig(ClusterMemberDiscoveryProtocolConfig config) {
            this.config.memberDiscoveryProtocolConfig = config;
            return this;
        }

        public Builder localMember(ClusterMember member) {
            config.localMember = member;
            return this;
        }

        public ClusterMembershipConfig build() {
            return config;
        }
    }
}
