package com.github.ynfeng.commander.engine.engine;

import com.github.ynfeng.commander.engine.context.ProcessContext;
import com.github.ynfeng.commander.engine.context.event.NodeExecuteEvent;
import com.github.ynfeng.commander.engine.event.EventStream;
import com.github.ynfeng.commander.engine.exception.ProcessEngineException;
import com.github.ynfeng.commander.engine.executor.NodeExecutor;
import com.github.ynfeng.commander.engine.executor.NodeExecutors;
import com.github.ynfeng.commander.definition.NodeDefinition;
import com.google.common.eventbus.Subscribe;
import java.util.Objects;

public final class ExecutorLauncher {
    private final NodeExecutors nodeExecutors;

    public ExecutorLauncher(NodeExecutors nodeExecutors) {
        this.nodeExecutors = nodeExecutors;
    }

    private static void checkExecutorNotNull(NodeExecutor nodeExecutor, String refName) {
        if (nodeExecutor == null) {
            throw new ProcessEngineException("Can't find any executor for " + refName);
        }
    }

    private static boolean isExecutable(NodeDefinition readyNode) {
        return !(NodeDefinition.NULL == readyNode || Objects.isNull(readyNode));
    }

    public void startUp() {
        EventStream.getInstance().subcribe(this);
    }

    @Subscribe
    public void handleEvent(NodeExecuteEvent event) {
        launchExecutor(event.processContext());
    }

    private void launchExecutor(ProcessContext context) {
        try {
            doLaunch(context);
        } catch (Exception e) {
            context.falied(e);
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
}
