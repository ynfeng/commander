package com.github.ynfeng.commander.cluster.communicate.protocol;

public enum ProtocolVersion {
    V1(1);
    private byte version;

    ProtocolVersion(int version) {
        this.version = (byte) version;
    }

    public byte version() {
        return version;
    }
}
