package com.github.ynfeng.commander.core.engine;

import com.github.ynfeng.commander.core.context.ProcessContext;
import com.github.ynfeng.commander.core.definition.NodeDefinition;
import com.github.ynfeng.commander.core.event.EngineEvent;
import com.github.ynfeng.commander.core.event.Event;
import com.github.ynfeng.commander.core.event.EventListener;
import com.github.ynfeng.commander.core.executor.NodeExecutor;
import com.github.ynfeng.commander.core.executor.NodeExecutors;

public final class ExecutorLauncher implements EventListener {
    private final NodeExecutors nodeExecutors;

    public ExecutorLauncher(NodeExecutors nodeExecutors) {
        this.nodeExecutors = nodeExecutors;
    }

    public void startUp() {
        EngineContext.registerListener(this);
    }

    @Override
    public void onEvent(Event event) {
        ProcessContext context = ((EngineEvent) event).context();
        launchExecutor(context);
    }

    private void launchExecutor(ProcessContext context) {
        NodeDefinition readyNode = context.readyNode();
        NodeExecutor nodeExecutor = nodeExecutors.getExecutor(readyNode);
        if (nodeExecutor == null) {
            throw new ProcessEngineException("Can't find any executor for " + readyNode.refName());
        }
        nodeExecutor.execute(context);
    }

    @Override
    public boolean interestedOn(Event event) {
        return event instanceof EngineEvent;
    }
}
