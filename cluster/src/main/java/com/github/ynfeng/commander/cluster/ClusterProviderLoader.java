package com.github.ynfeng.commander.cluster;

import java.util.Optional;

public interface ClusterProviderLoader {
    Optional<ClusterProvider> load();
}
