package com.github.ynfeng.commander.core.engine;

import com.github.ynfeng.commander.core.context.ProcessContext;
import com.github.ynfeng.commander.core.event.EngineEvent;
import com.github.ynfeng.commander.core.event.Event;
import com.github.ynfeng.commander.core.event.EventListener;
import com.github.ynfeng.commander.core.executor.NodeExecutor;
import com.github.ynfeng.commander.core.executor.NodeExecutors;

public final class ExecutorLauncher implements EventListener {
    private final NodeExecutors nodeExecutors = new NodeExecutors();

    public ExecutorLauncher() {

    }

    public void startUp() {
        EngineContext.registerListener(this);
        nodeExecutors.load();
    }

    @Override
    public void onEvent(Event event) {
        ProcessContext context = ((EngineEvent) event).context();
        NodeExecutor nodeExecutor = nodeExecutors.getExecutor(context.currentNode());
        nodeExecutor.execute(context);
    }

    @Override
    public boolean interestedOn(Event event) {
        return event instanceof EngineEvent;
    }
}
