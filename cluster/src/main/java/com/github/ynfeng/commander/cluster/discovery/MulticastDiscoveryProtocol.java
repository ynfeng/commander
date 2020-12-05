package com.github.ynfeng.commander.cluster.discovery;

public class MulticastDiscoveryProtocol implements NodeDiscoveryProtocol {
    private final MulticastDiscoveryConfig config;
    public static final Type TYPE = new Type();

    public MulticastDiscoveryProtocol(MulticastDiscoveryConfig config) {
        this.config = config;
    }

    public static class Type
        implements NodeDiscoveryProtocolType<MulticastDiscoveryConfig, MulticastDiscoveryProtocol> {

        @Override
        public MulticastDiscoveryProtocol newProtocol(MulticastDiscoveryConfig config) {
            return new MulticastDiscoveryProtocol(config);
        }
    }
}
