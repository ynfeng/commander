package com.github.ynfeng.commander.cluster.discovery.impl;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.github.ynfeng.commander.cluster.ClusterNode;
import com.github.ynfeng.commander.cluster.discovery.NodeDiscoveryMessage;
import com.github.ynfeng.commander.cluster.discovery.NodeDiscoveryProtocol;
import com.github.ynfeng.commander.support.Address;
import com.github.ynfeng.commander.support.Host;
import java.util.concurrent.CompletableFuture;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MulticastDiscoveryProtocolTypeTest {
    private NodeDiscoveryProtocol protocol;

    @BeforeEach
    public void setup() {
        MulticastDiscoveryProtocolConfig config = MulticastDiscoveryProtocolConfig.builder()
            .localHost(Host.of("127.0.0.1"))
            .groupAddress(Address.of("230.0.0.1", 1234))
            .localNode(ClusterNode.of("testNode"))
            .broadcastInterval(10)
            .build();
        protocol = config.protocolType().newProtocol();
        protocol.start();
    }

    @AfterEach
    public void tearDown() {
        protocol.shutdown();
    }

    @Test
    public void should_broadcast_message_when_node_online() {
        CompletableFuture<ClusterNode> future = new CompletableFuture<ClusterNode>();
        protocol.addClusterNodeChangeListener(NodeDiscoveryMessage.Type.Online, node -> {
            future.complete(node);
        });

        assertThat(future.join().name(), is("testNode"));
    }

    @Test
    public void should_broadcast_message_when_node_offline() {
        CompletableFuture<ClusterNode> future = new CompletableFuture<ClusterNode>();
        protocol.addClusterNodeChangeListener(NodeDiscoveryMessage.Type.Offline, node -> {
            future.complete(node);
        });
        protocol.broadcastOffline();

        assertThat(future.join().name(), is("testNode"));
    }
}
