package com.github.ynfeng.commander.core.context;

import com.google.common.collect.Maps;
import java.util.Map;

public class ConcurrentProcessContexts implements ProcessContexts {
    private final Map<ProcessId, ProcessContext> contexts = Maps.newConcurrentMap();

    public ConcurrentProcessContexts() {

    }

    @Override
    public void add(ProcessContext processContext) {
        contexts.put(processContext.processId(), processContext);
    }

    @Override
    public int size() {
        return contexts.size();
    }

    @Override
    public void remove(ProcessContext processContext) {
        contexts.remove(processContext.processId());
    }
}
