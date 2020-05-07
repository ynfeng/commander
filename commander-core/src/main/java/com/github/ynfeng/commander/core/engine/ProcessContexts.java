package com.github.ynfeng.commander.core.engine;

import com.google.common.collect.Maps;
import java.util.Map;

public final class ProcessContexts {
    private final Map<ProcessId, ProcessContext> contexts = Maps.newHashMap();

    protected ProcessContexts() {

    }

    public void add(ProcessContext processContext) {
        contexts.put(processContext.processId(), processContext);
    }

    public ProcessContext get(ProcessId processId) {
        return contexts.get(processId);
    }
}
