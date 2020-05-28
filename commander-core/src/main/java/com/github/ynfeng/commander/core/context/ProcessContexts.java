package com.github.ynfeng.commander.core.context;

import com.google.common.collect.Maps;
import java.util.Map;

public final class ProcessContexts {
    private final Map<ProcessId, ProcessContext> contexts = Maps.newConcurrentMap();

    public ProcessContexts() {

    }

    public void add(ProcessContext processContext) {
        contexts.put(processContext.processId(), processContext);
    }

    public int size() {
        return contexts.size();
    }

    public void remove(ProcessContext processContext) {
        contexts.remove(processContext.processId());
    }
}
