package com.github.ynfeng.commander.cluster.atomix;

import com.github.ynfeng.commander.cluster.ClusterContext;
import com.github.ynfeng.commander.support.env.Environment;
import io.atomix.core.Atomix;

public class AtomixClusterContext implements ClusterContext {
    private final Atomix atomix;
    private final Environment env;

    protected AtomixClusterContext(Atomix atomix, Environment env) {
        this.atomix = atomix;
        this.env = env;
    }
}
