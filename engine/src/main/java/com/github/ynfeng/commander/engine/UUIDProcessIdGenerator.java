package com.github.ynfeng.commander.engine;

import java.util.UUID;

public class UUIDProcessIdGenerator implements ProcessIdGenerator {
    @Override
    public ProcessId nextId() {
        return ProcessId.of(UUID.randomUUID().toString());
    }
}
