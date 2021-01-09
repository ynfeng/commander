package com.github.ynfeng.commander.cluster;

import java.util.Objects;

public class ClusterMember {
    private String name;

    @SuppressWarnings("unused")
    private ClusterMember() {

    }

    private ClusterMember(String name) {
        this.name = name;
    }

    public static ClusterMember of(String name) {
        return new ClusterMember(name);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ClusterMember)) {
            return false;
        }

        ClusterMember that = (ClusterMember) o;

        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        if (name != null) {
            return name.hashCode();
        }
        return 0;
    }
}
