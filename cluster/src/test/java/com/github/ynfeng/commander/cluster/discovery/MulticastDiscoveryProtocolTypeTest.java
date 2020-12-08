package com.github.ynfeng.commander.cluster.discovery;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.Test;

class MulticastDiscoveryProtocolTypeTest {

    @Test
    public void should_create_multicast_discovery_protocol() {
        MulticastDiscoveryConfig config = new MulticastDiscoveryConfig();
        MulticastDiscoveryProtocol protocol = MulticastDiscoveryProtocol.TYPE.newProtocol(config);

        assertThat(protocol, notNullValue());
    }

    @Test
    public void should_startup_and_shutdown() {
        MulticastDiscoveryConfig config = new MulticastDiscoveryConfig();
        MulticastDiscoveryProtocol protocol = MulticastDiscoveryProtocol.TYPE.newProtocol(config);
        protocol.start();

        assertThat(protocol.isStarted(), is(true));

        protocol.shutdown();
        assertThat(protocol.isStarted(), is(false));
    }
}
