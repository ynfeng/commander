package com.github.ynfeng.commander.cluster;

public interface StorageProvider {
    ConsistentStore getConsistentStore(String name);
}
