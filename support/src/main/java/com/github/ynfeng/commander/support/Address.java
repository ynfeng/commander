package com.github.ynfeng.commander.support;

import java.net.InetSocketAddress;

public class Address {
    private Host host;
    private int port;

    @SuppressWarnings("unused")
    private Address() {
    }

    private Address(String host, int port) {
        this.host = Host.of(host);
        this.port = port;
    }

    public static Address of(String ip, int port) {
        return new Address(ip, port);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Address)) {
            return false;
        }

        Address address = (Address) o;

        if (port != address.port) {
            return false;
        }
        return host.equals(address.host);
    }

    @Override
    public int hashCode() {
        int result = host.hashCode();
        result = 31 * result + port;
        return result;
    }

    public InetSocketAddress toInetSocketAddress() {
        return new InetSocketAddress(host.ip(), port);
    }

    public int port() {
        return port;
    }

    public String host() {
        return host.ip();
    }
}
