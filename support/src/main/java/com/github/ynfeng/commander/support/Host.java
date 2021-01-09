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
        byte[] addrBytes = new byte[4];
        String[] ipItems = ip.split("\\.");
        for (int i = 0; i < addrBytes.length; i++) {
            addrBytes[i] = Byte.valueOf(ipItems[i]);
        }
        return addrBytes;
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
