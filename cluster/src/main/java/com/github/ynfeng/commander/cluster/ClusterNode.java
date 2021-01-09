package com.github.ynfeng.commander.cluster;

public class ClusterNode {
    private String name;

    @SuppressWarnings("unused")
    private ClusterNode() {

    }

    private ClusterNode(String name) {
        this.name = name;
    }

    public static ClusterNode of(String name) {
        return new ClusterNode(name);
    }

    public String name() {
        return name;
    }

    @Override
    public String toString() {
        return "ClusterNode{"
            + "name='" + name + '\''
            + '}';
    }
}
