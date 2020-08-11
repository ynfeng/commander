package com.github.ynfeng.commander.support;

import com.google.common.base.Objects;

public class Address {
    private final String ip;
    private final int port;

    private Address(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public static Address of(String ip, int port) {
        return new Address(ip, port);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Address) {
            Address that = (Address) obj;
            return Objects.equal(ip, that.ip) && Objects.equal(port, that.port);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(ip, port);
    }
}
