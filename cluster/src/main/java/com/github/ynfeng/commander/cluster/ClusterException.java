package com.github.ynfeng.commander.cluster;

public class ClusterException extends RuntimeException {
    public ClusterException(Throwable t) {
        super(t);
    }

    public ClusterException(String msg) {
        super(msg);
    }

    public ClusterException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
