package com.github.ynfeng.commander.core.engine;

import com.github.ynfeng.commander.core.context.ProcessContext;
import com.github.ynfeng.commander.core.definition.NodeDefinition;
import com.github.ynfeng.commander.core.event.EngineEvent;
import com.github.ynfeng.commander.core.event.Event;
import com.github.ynfeng.commander.core.event.EventListener;
import com.github.ynfeng.commander.core.eventbus.ProcessEngineEventBus;
import com.github.ynfeng.commander.core.exception.ProcessEngineException;
import com.github.ynfeng.commander.core.executor.NodeExecutor;
import com.github.ynfeng.commander.core.executor.NodeExecutors;
import java.util.Objects;

public final class ExecutorLauncher implements EventListener {
    private final NodeExecutors nodeExecutors;

    public ExecutorLauncher(NodeExecutors nodeExecutors) {
        this.nodeExecutors = nodeExecutors;
    }

    public void startUp() {
        ProcessEngineEventBus.getInstance().registerListener(this);
    }

    @Override
    public void onEvent(Event event) {
        ProcessContext context = ((EngineEvent) event).context();
        launchExecutor(context);
    }

    private void launchExecutor(ProcessContext context) {
        try {
            doLaunch(context);
        } catch (Exception e) {
            context.executeException(e);
            context.complete();
        }
    }

    private void doLaunch(ProcessContext context) {
        NodeDefinition readyNode = context.readyNode();
        while (isExecutable(readyNode)) {
            NodeExecutor nodeExecutor = nodeExecutors.getExecutor(readyNode);
            checkExecutorNotNull(nodeExecutor, readyNode.refName());
            nodeExecutor.execute(context, readyNode);
            readyNode = context.readyNode();
        }
    }


    private static void checkExecutorNotNull(NodeExecutor nodeExecutor, String refName) {
        if (nodeExecutor == null) {
            throw new ProcessEngineException("Can't find any executor for " + refName);
        }
    }

    private static boolean isExecutable(NodeDefinition readyNode) {
        return !(NodeDefinition.NULL == readyNode || Objects.isNull(readyNode));
    }

    @Override
    public boolean interestedOn(Event event) {
        return event instanceof EngineEvent;
    }
}
