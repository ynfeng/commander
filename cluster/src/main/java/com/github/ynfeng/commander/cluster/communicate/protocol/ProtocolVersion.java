package com.github.ynfeng.commander.cluster.communicate.protocol;

import java.util.Arrays;
import java.util.Optional;

public enum ProtocolVersion {
    V1(1);
    private byte version;

    ProtocolVersion(int version) {
        this.version = (byte) version;
    }

    public static Optional<ProtocolVersion> valueOf(byte version) {
        return Arrays.stream(values())
            .filter(pv -> pv.version == version)
            .findFirst();
    }

    public byte version() {
        return version;
    }
}
