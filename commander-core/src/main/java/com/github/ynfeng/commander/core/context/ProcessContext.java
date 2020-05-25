package com.github.ynfeng.commander.core.context;


import com.github.ynfeng.commander.core.definition.NodeDefinition;
import com.github.ynfeng.commander.core.definition.ProcessDefinition;
import com.github.ynfeng.commander.core.event.NodeExecuteCompleteEvent;
import com.github.ynfeng.commander.core.eventbus.ProcessEngineEventBus;
import com.github.ynfeng.commander.core.event.ProcessExecuteCompleteEvent;
import java.util.Objects;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ProcessContext {
    private final ProcessId processId;
    private final ProcessDefinition processDefinition;
    private volatile ProcessStatus processStatus;
    private final ConcurrentLinkedQueue<NodeDefinition> readyQueue = new ConcurrentLinkedQueue<NodeDefinition>();
    private Throwable executeException;

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

    private <T extends NodeDefinition> void addReadyNode(T next) {
        readyQueue.offer(next);
    }

    public void running() {
        processStatus = ProcessStatus.RUNNING;
    }

    public void complete() {
        processStatus = ProcessStatus.COMPLETED;
        ProcessEngineEventBus.getInstance().publishEvent(ProcessExecuteCompleteEvent.create(this));
    }

    public void completeNode(NodeDefinition next) {
        addReadyNode(next);
        ProcessEngineEventBus.getInstance().publishEvent(NodeExecuteCompleteEvent.create(this));
    }

    public void executeException(Throwable t) {
        executeException = t;
    }


    public boolean hasException() {
        return Objects.nonNull(executeException);
    }

    public Throwable executeException() {
        return executeException;
    }
}
