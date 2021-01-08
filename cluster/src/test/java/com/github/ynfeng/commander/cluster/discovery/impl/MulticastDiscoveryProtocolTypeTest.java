package com.github.ynfeng.commander.cluster.discovery.impl;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.github.ynfeng.commander.cluster.discovery.NodeDiscoveryProtocol;
import com.github.ynfeng.commander.support.Address;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class MulticastDiscoveryProtocolTypeTest {
    private MulticastDiscoveryConfig config;

    @BeforeEach
    public void setup() {
        MulticastDiscoveryConfig config = new MulticastDiscoveryConfig();
        this.config = Mockito.spy(config);
        Mockito.when(this.config.localAddress()).thenReturn(Address.of("127.0.0.1", 1234));
        Mockito.when(this.config.groupAddress()).thenReturn(Address.of("230.0.0.1", 1234));
    }

    @Test
    public void should_create_multicast_discovery_protocol() {
        NodeDiscoveryProtocol discoveryProtocol = config.protocolType().newProtocol();

        assertThat(discoveryProtocol, instanceOf(MulticastDiscoveryProtocol.class));
    }

    @Test
    public void should_startup_and_shutdown() {
        NodeDiscoveryProtocol protocol = config.protocolType().newProtocol();
        protocol.start();
        protocol.start();

        assertThat(protocol.isStarted(), is(true));

        protocol.shutdown();
        protocol.shutdown();
        assertThat(protocol.isStarted(), is(false));
    }
}
