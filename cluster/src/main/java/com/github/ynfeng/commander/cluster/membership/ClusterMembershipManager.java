package com.github.ynfeng.commander.cluster.membership;

import com.github.ynfeng.commander.cluster.ClusterMember;
import com.github.ynfeng.commander.cluster.discovery.ClusterMemberDiscoveryMessage;
import com.github.ynfeng.commander.cluster.discovery.ClusterMemberDiscoveryProtocol;
import com.github.ynfeng.commander.support.Manageable;
import com.google.common.collect.Sets;
import java.util.Collections;
import java.util.Set;

public class ClusterMembershipManager implements Manageable {
    private final ClusterMembershipConfig config;
    private final ClusterMemberDiscoveryProtocol clusterMemberDiscoveryProtocol;
    private final Set<ClusterMember> members = Sets.newConcurrentHashSet();

    public ClusterMembershipManager(ClusterMembershipConfig config) {
        this.config = config;
        clusterMemberDiscoveryProtocol = config.memberDiscoveryProtocolConfig().protocolType().newProtocol();
    }

    @Override
    public void start() {
        clusterMemberDiscoveryProtocol.start();
        clusterMemberDiscoveryProtocol.addClusterNodeChangeListener(
            ClusterMemberDiscoveryMessage.Type.Online,
            member -> memberOnline(member));
        clusterMemberDiscoveryProtocol.addClusterNodeChangeListener(
            ClusterMemberDiscoveryMessage.Type.Offline,
            member -> memberOffline(member));
    }

    private void memberOffline(ClusterMember member) {
        if (!member.equals(config.localMember())) {
            members.remove(member);
        }
    }

    private void memberOnline(ClusterMember member) {
        if (!member.equals(config.localMember())) {
            members.add(member);
        }
    }

    @Override
    public void shutdown() {
        clusterMemberDiscoveryProtocol.shutdown();
    }

    @Override
    public boolean isStarted() {
        return false;
    }

    public Set<ClusterMember> clusterMembers() {
        return Collections.unmodifiableSet(members);
    }
}
