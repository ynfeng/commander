package com.github.ynfeng.commander.cluster.discovery.impl;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.github.ynfeng.commander.cluster.ClusterNode;
import com.github.ynfeng.commander.cluster.discovery.NodeDiscoveryMessage;
import com.github.ynfeng.commander.cluster.discovery.NodeDiscoveryProtocol;
import com.github.ynfeng.commander.support.Address;
import java.util.concurrent.CompletableFuture;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class MulticastDiscoveryProtocolTypeTest {
    private MulticastDiscoveryConfig config;
    private NodeDiscoveryProtocol protocol;

    @BeforeEach
    public void setup() {
        MulticastDiscoveryConfig config = new MulticastDiscoveryConfig();
        this.config = Mockito.spy(config);
        Mockito.when(this.config.localAddress()).thenReturn(Address.of("127.0.0.1", 1234));
        Mockito.when(this.config.groupAddress()).thenReturn(Address.of("230.0.0.1", 1234));
        Mockito.when(this.config.localNode()).thenReturn(ClusterNode.of("testNode"));
        protocol = this.config.protocolType().newProtocol();
        protocol.start();
    }

    @AfterEach
    public void tearDown() {
        protocol.shutdown();
    }

    @Test
    public void should_broadcast_message_when_node_online() {
        CompletableFuture<ClusterNode> future = new CompletableFuture<ClusterNode>();
        protocol.addListener(NodeDiscoveryMessage.Type.Online, node -> {
            future.complete(node);
        });

        assertThat(future.join().name(), is("testNode"));
    }
}
