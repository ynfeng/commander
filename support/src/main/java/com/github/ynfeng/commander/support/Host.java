package com.github.ynfeng.commander.support;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class Host {
    private String ip;

    @SuppressWarnings("unused")
    private Host() {

    }

    public Host(String ip) {
        this.ip = ip;
    }

    public static Host of(String ip) {
        return new Host(ip);
    }

    byte[] ipToBytes() {
        try {
            return InetAddress.getByName(ip).getAddress();
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }

    public String ip() {
        return ip;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Host)) {
            return false;
        }

        Host host = (Host) o;

        return ip.equals(host.ip);
    }

    @Override
    public int hashCode() {
        return ip.hashCode();
    }

    public InetAddress toInetAddress() {
        try {
            return InetAddress.getByAddress(ipToBytes());
        } catch (UnknownHostException e) {
            return null;
        }
    }
}
