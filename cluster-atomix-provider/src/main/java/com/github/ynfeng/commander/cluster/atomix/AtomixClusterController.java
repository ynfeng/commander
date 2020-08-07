package com.github.ynfeng.commander.cluster.atomix;


import com.github.ynfeng.commander.cluster.AbstractClusterController;
import com.github.ynfeng.commander.cluster.config.ClusterConfig;
import com.github.ynfeng.commander.cluster.config.NodeConfig;
import io.atomix.cluster.Node;
import io.atomix.cluster.discovery.BootstrapDiscoveryProvider;
import io.atomix.core.Atomix;
import io.atomix.utils.net.Address;

public class AtomixClusterController extends AbstractClusterController {
    private final ClusterConfig clusterConfig;
    private final NodeConfig nodeConfig;

    public AtomixClusterController(ClusterConfig clusterConfig, NodeConfig nodeConfig) {
        this.clusterConfig = clusterConfig;
        this.nodeConfig = nodeConfig;
    }

    @SuppressWarnings("checkstyle:MethodLength")
    @Override
    public void start() {
        Atomix atomix = Atomix.builder()
            .withAddress(Address.from(nodeConfig.address(), 18089))
            .withMembershipProvider(BootstrapDiscoveryProvider.builder()
                .withNodes(
                    Node.builder()
                        .withId(nodeConfig.nodeId())
                        .withAddress(Address.from(nodeConfig.address(), 18089))
                        .build())
                .build())
            .build();
        atomix.start();
    }

    @Override
    public void stop() {
    }
}
