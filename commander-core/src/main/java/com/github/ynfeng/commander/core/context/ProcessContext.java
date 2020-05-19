package com.github.ynfeng.commander.core.context;


import com.github.ynfeng.commander.core.definition.NodeDefinition;
import com.github.ynfeng.commander.core.definition.ProcessDefinition;
import com.github.ynfeng.commander.core.engine.EngineContext;
import com.github.ynfeng.commander.core.event.NodeExecuteCompleteEvent;
import com.github.ynfeng.commander.core.event.ProcessExecuteCompleteEvent;
import java.util.concurrent.ConcurrentLinkedQueue;

public final class ProcessContext {
    private final ProcessId processId;
    private final ProcessDefinition processDefinition;
    private ProcessStatus processStatus;
    private final ConcurrentLinkedQueue<NodeDefinition> readyQueue = new ConcurrentLinkedQueue();

    public ProcessContext(ProcessId processId, ProcessDefinition processDefinition) {
        this.processId = processId;
        this.processDefinition = processDefinition;
        processStatus = ProcessStatus.CREATED;
        readyQueue.offer(processDefinition.firstNode());
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
        return (T) readyQueue.poll();
    }

    private <T extends NodeDefinition> void nextNode(T next) {
        readyQueue.offer(next);
    }

    public void running() {
        processStatus = ProcessStatus.RUNNING;
    }

    public void done() {
        processStatus = ProcessStatus.COMPLETED;
        EngineContext.publishEvent(ProcessExecuteCompleteEvent.create(this));
    }

    public void completeReadyNode(NodeDefinition next) {
        nextNode(next);
        EngineContext.publishEvent(NodeExecuteCompleteEvent.create(this));
    }
}
