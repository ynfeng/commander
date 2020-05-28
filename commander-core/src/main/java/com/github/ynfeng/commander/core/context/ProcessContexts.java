package com.github.ynfeng.commander.core.context;

import com.google.common.collect.Maps;
import java.util.Map;

public final class ProcessContexts {
    private final Map<ProcessId, ProcessContext> contexts = Maps.newConcurrentMap();

    public ProcessContexts() {

    }

    public void add(ProcessContext processContext) {
        System.out.println("add:" + processContext.processDefinition().name());
        contexts.put(processContext.processId(), processContext);
    }

    public int size() {
        return contexts.size();
    }

    public void remove(ProcessContext processContext) {
        System.out.println("remove:" + processContext.processDefinition().name());
        contexts.remove(processContext.processId());
        System.out.println("after remove:" + contexts.get(processContext.processId().toString()));
        System.out.println(contexts.size());
    }
}
