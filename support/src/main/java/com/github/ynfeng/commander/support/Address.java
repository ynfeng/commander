package com.github.ynfeng.commander.support;

import com.google.common.base.Objects;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

public class Address {
    private String host;
    private int port;

    private Address() {
    }

    private Address(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public static Address of(String ip, int port) {
        return new Address(ip, port);
    }

    public InetAddress toInetAddress() {
        try {
            return InetAddress.getByAddress(ipToBytes());
        } catch (UnknownHostException e) {
            return null;
        }
    }

    private byte[] ipToBytes() {
        byte[] addrBytes = new byte[4];
        String[] ipItems = host.split("\\.");
        for (int i = 0; i < addrBytes.length; i++) {
            addrBytes[i] = Byte.valueOf(ipItems[i]);
        }
        return addrBytes;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Address) {
            Address that = (Address) obj;
            return Objects.equal(host, that.host) && Objects.equal(port, that.port);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(host, port);
    }

    public InetSocketAddress toInetSocketAddress() {
        return new InetSocketAddress(host, port);
    }

    public int port() {
        return port;
    }

    public String host() {
        return host;
    }
}
