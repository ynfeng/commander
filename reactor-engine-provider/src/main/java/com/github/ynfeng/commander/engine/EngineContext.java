package com.github.ynfeng.commander.engine;

import com.google.common.collect.Maps;
import java.util.Map;
import java.util.Optional;

public class EngineContext {
    private final Map<ProcessId, ReactorProcessInstance> instances = Maps.newConcurrentMap();

    public void addProcessInstance(ReactorProcessInstance instance) {
        instances.put(instance.processId(), instance);
    }

    public Optional<ReactorProcessInstance> getProcessInstance(ProcessId processId) {
        return Optional.ofNullable(instances.get(processId));
    }
}
