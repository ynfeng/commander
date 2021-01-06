package com.github.ynfeng.commander.cluster.discovery.impl;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;

import com.github.ynfeng.commander.cluster.discovery.NodeDiscoveryProtocol;
import org.junit.jupiter.api.Test;

class MulticastDiscoveryProtocolTypeTest {

    @Test
    public void should_create_multicast_discovery_protocol() {
        MulticastDiscoveryConfig config = new MulticastDiscoveryConfig();
        NodeDiscoveryProtocol discoveryProtocol = config.protocolType().newProtocol(config);

        assertThat(discoveryProtocol, notNullValue());
    }

    @Test
    public void should_startup_and_shutdown() {
        MulticastDiscoveryConfig config = new MulticastDiscoveryConfig();
        NodeDiscoveryProtocol protocol = config.protocolType().newProtocol(config);
        protocol.start();
        protocol.start();

        assertThat(protocol.isStarted(), is(true));

        protocol.shutdown();
        protocol.shutdown();
        assertThat(protocol.isStarted(), is(false));
    }
}
