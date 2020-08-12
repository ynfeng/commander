package com.github.ynfeng.commander.cluster.atomix;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.google.common.collect.Lists;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;

class AtomixEnvTest {

    @Test
    public void should_get_property() {
        AtomixEnv env = new AtomixEnv();

        String clusterId = env.getProperty(PropertyKey.CLUSTER_ID, "default");
        String nodeId = env.getProperty(PropertyKey.CLUSTER_NODE_ID, "default");
        String address = env.getProperty(PropertyKey.CLUSTER_NODE_ADDRESS, "default");
        int port = env.getProperty(PropertyKey.CLUSTER_NODE_PORT, 0);
        int mgrPartitions = env.getProperty(PropertyKey.CLUSTER_MGR_PARTITIONS, 0);
        String mgrDataDir = env.getProperty(PropertyKey.CLUSTER_MGR_DATA_DIR, "/tmp");
        List<String> mgrGroupMembers = env.getProperty(PropertyKey.CLUSTER_MGR_GROUP_MEMBERS, Lists.newArrayList());
        int bootstrapDiscoveryBroadcastIntervalSeconds =
            env.getProperty(PropertyKey.CLUSTER_BOOTSTRAP_DISCOVERY_BROADCAST_INTERVAL_SECONDS, 200);
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
    }

    @Test
    public void should_return_default_value_when_property_not_exists() {
        AtomixEnv atomixEnv = new AtomixEnv();

        String notExist = atomixEnv.getProperty("notExists", "notExist");

        assertThat(notExist, is("notExist"));
    }

    @Test
    public void should_return_default_value_when_property_was_null() {
        AtomixEnv atomixEnv = new AtomixEnv();

        String shouldNull = atomixEnv.getProperty("cluster.shouldNull", "shouldNull");

        assertThat(shouldNull, is("shouldNull"));
    }
}
