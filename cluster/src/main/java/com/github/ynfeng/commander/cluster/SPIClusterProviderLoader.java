package com.github.ynfeng.commander.cluster;

import java.util.Iterator;
import java.util.Optional;
import java.util.ServiceLoader;

public final class SPIClusterProviderLoader implements ClusterProviderLoader {
    public SPIClusterProviderLoader() {
    }

    @Override
    public Optional<ClusterProvider> load() {
        ServiceLoader<ClusterProvider> clusterProviderFactories = ServiceLoader.load(ClusterProvider.class);
        Iterator<ClusterProvider> it = clusterProviderFactories.iterator();
        if (it.hasNext()) {
            return Optional.of(it.next());
        }
        return Optional.empty();
    }
}
