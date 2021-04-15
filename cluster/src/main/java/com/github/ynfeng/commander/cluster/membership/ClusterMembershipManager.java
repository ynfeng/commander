package com.github.ynfeng.commander.cluster.membership;

import com.github.ynfeng.commander.cluster.ClusterMember;
import com.github.ynfeng.commander.cluster.discovery.ClusterMemberDiscoveryMessage;
import com.github.ynfeng.commander.cluster.discovery.ClusterMemberDiscoveryProtocol;
import com.github.ynfeng.commander.support.ManageableSupport;
import com.google.common.collect.Sets;
import java.util.Collections;
import java.util.Set;

public class ClusterMembershipManager extends ManageableSupport {
    private final ClusterMembershipConfig config;
    private final ClusterMemberDiscoveryProtocol clusterMemberDiscoveryProtocol;
    private final Set<ClusterMember> members = Sets.newConcurrentHashSet();

    public ClusterMembershipManager(ClusterMembershipConfig config) {
        this.config = config;
        clusterMemberDiscoveryProtocol = config.memberDiscoveryProtocolConfig().protocolType().newProtocol();
    }

    @Override
    public void doStart() {
        clusterMemberDiscoveryProtocol.start();
        clusterMemberDiscoveryProtocol.addClusterNodeChangeListener(
            ClusterMemberDiscoveryMessage.Type.ONLINE, this::memberOnline);
        clusterMemberDiscoveryProtocol.addClusterNodeChangeListener(
            ClusterMemberDiscoveryMessage.Type.OFFLINE, this::memberOffline);
    }

    private void memberOffline(ClusterMember member) {
        if (!isLocalMember(member)) {
            members.remove(member);
        }
    }

    private boolean isLocalMember(ClusterMember member) {
        return member.equals(config.localMember());
    }

    private void memberOnline(ClusterMember member) {
        if (!isLocalMember(member)) {
            members.add(member);
        }
    }

    @Override
    public void doShutdown() {
        clusterMemberDiscoveryProtocol.shutdown();
    }

    public Set<ClusterMember> clusterMembers() {
        return Collections.unmodifiableSet(members);
    }
}
