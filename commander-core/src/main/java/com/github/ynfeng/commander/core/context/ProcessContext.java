package com.github.ynfeng.commander.core.context;


import com.github.ynfeng.commander.core.definition.NodeDefinition;
import com.github.ynfeng.commander.core.definition.ProcessDefinition;
import com.github.ynfeng.commander.core.engine.EngineContext;
import com.github.ynfeng.commander.core.event.NodeExecuteCompleteEvent;
import com.github.ynfeng.commander.core.event.ProcessExecuteCompleteEvent;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import java.util.Collections;
import java.util.List;

public final class ProcessContext {
    private final ProcessId processId;
    private final ProcessDefinition processDefinition;
    private final List<String> executedNodes = Lists.newArrayList();
    private ProcessStatus processStatus;
    private NodeDefinition currentNode;

    public ProcessContext(ProcessId processId, ProcessDefinition processDefinition) {
        this.processId = processId;
        this.processDefinition = processDefinition;
        processStatus = ProcessStatus.CREATED;
        currentNode = processDefinition.firstNode();
    }

    public ProcessId processId() {
        return processId;
    }

    public ProcessDefinition processDefinition() {
        return processDefinition;
    }

    public ProcessStatus status() {
        return processStatus;
    }

    @SuppressWarnings("unchecked")
    public <T extends NodeDefinition> T readyNode() {
        return (T) currentNode;
    }


    public List<String> executedNodes() {
        return Collections.unmodifiableList(executedNodes);
    }

    private <T extends NodeDefinition> void nextNode(T next) {
        currentNode = next;
    }

    public void running() {
        processStatus = ProcessStatus.RUNNING;
    }

    public void done() {
        processStatus = ProcessStatus.COMPLETED;
        EngineContext.publishEvent(ProcessExecuteCompleteEvent.create(this));
    }

    public void completeCurrentNode(NodeDefinition next) {
        addExecutedNode(currentNode.refName());
        nextNode(next);
        EngineContext.publishEvent(NodeExecuteCompleteEvent.create(this));
    }

    private void addExecutedNode(String refName) {
        if (!Strings.isNullOrEmpty(refName)) {
            executedNodes.add(refName);
        }
    }
}
