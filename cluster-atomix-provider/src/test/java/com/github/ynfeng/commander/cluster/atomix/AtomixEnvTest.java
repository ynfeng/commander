package com.github.ynfeng.commander.cluster.atomix;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import com.google.common.collect.Lists;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AtomixEnvTest {

    private AtomixEnv env;

    @BeforeEach
    public void setup() {
        System.setProperty("cmder.profile", "test");
        env = new AtomixEnv(new YamlPropertySource());
    }

    @Test
    public void should_get_property() {
        String clusterId = env.getProperty(PropertyKey.CLUSTER_ID, "default");
        String nodeId = env.getProperty(PropertyKey.CLUSTER_NODE_ID, "default");
        String address = env.getProperty(PropertyKey.CLUSTER_NODE_ADDRESS, "default");
        int port = env.getProperty(PropertyKey.CLUSTER_NODE_PORT, 0);
        int mgrPartitions = env.getProperty(PropertyKey.CLUSTER_MGR_PARTITIONS, 0);
        String mgrDataDir = env.getProperty(PropertyKey.CLUSTER_MGR_DATA_DIR, "/tmp");
        List<String> mgrGroupMembers = env.getProperty(PropertyKey.CLUSTER_MGR_GROUP_MEMBERS, Lists.newArrayList());
        int bootstrapDiscoveryBroadcastIntervalSeconds =
            env.getProperty(PropertyKey.CLUSTER_DISCOVERY_BROADCAST_INTERVAL_SECONDS, 200);
        int membershipFailureTimeOutSeconds =
            env.getProperty(PropertyKey.CLUSTER_MEMBERSHIP_FAILURE_TIME_OUT_SECONDS, 100);
        boolean memberBroadcastDisputes =
            env.getProperty(PropertyKey.CLUSTER_MEMBERSHIP_BROADCAST_DISPUTES, false);
        assertThat(clusterId, is("test"));
        assertThat(nodeId, is("local"));
        assertThat(address, is("127.0.0.1"));
        assertThat(port, is(12345));
        assertThat(mgrPartitions, is(1));
        assertThat(mgrDataDir, is("/tmp/mgr-data"));
        assertThat(mgrGroupMembers, is(Arrays.asList("local")));
        assertThat(bootstrapDiscoveryBroadcastIntervalSeconds, is(5));
        assertThat(membershipFailureTimeOutSeconds, is(5));
        assertThat(memberBroadcastDisputes, is(true));
        assertThat(env.name(), is("atomix"));
    }

    @Test
    public void should_return_default_value_when_property_not_exists() {
        String notExist = env.getProperty("notExists", "notExist");

        assertThat(notExist, is("notExist"));
    }

    @Test
    public void should_return_default_value_when_property_was_null() {
        String shouldNull = env.getProperty("cluster.shouldNull", "shouldNull");

        assertThat(shouldNull, is("shouldNull"));
    }

    @Test
    public void should_return_null_value_when_preperty_was_null() {
        String shouldNull = env.getProperty("cluster.shouldNull");

        assertThat(shouldNull, nullValue());
    }

    @Test
    public void should_return_null_value_when_property_not_exists() {
        String shouldNull = env.getProperty("cluster.notExists");

        assertThat(shouldNull, nullValue());
    }
}
