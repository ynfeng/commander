package com.github.ynfeng.commander.engine.context;


import com.github.ynfeng.commander.definition.NodeDefinition;
import com.github.ynfeng.commander.definition.ProcessDefinition;
import com.github.ynfeng.commander.engine.Variables;
import com.github.ynfeng.commander.engine.context.event.NodeExecuteCompletedEvent;
import com.github.ynfeng.commander.engine.context.event.ProcessExecuteCompletedEvent;
import com.github.ynfeng.commander.engine.context.event.ProcessExecuteFailedEvent;
import com.github.ynfeng.commander.engine.event.EventStream;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

public class ProcessContext {
    private final ProcessId processId;
    private final ProcessDefinition processDefinition;
    private final ConcurrentLinkedQueue<NodeDefinition> readyQueue = new ConcurrentLinkedQueue<NodeDefinition>();
    private final ConcurrentLinkedQueue<String> executedNodes = new ConcurrentLinkedQueue<String>();
    private final Variables input = new Variables();
    private volatile ProcessStatus processStatus;
    private Throwable executeException;

    private ProcessContext(ProcessId processId, ProcessDefinition processDefinition) {
        this.processId = processId;
        this.processDefinition = processDefinition;
        processStatus = ProcessStatus.CREATED;
        readyQueue.offer(processDefinition.firstNode());
    }

    public static ProcessContext create(ProcessId processId, ProcessDefinition processDefinition) {
        ProcessContext processContext = new ProcessContext(processId, processDefinition);
        return processContext;
    }

    public ProcessId processId() {
        return processId;
    }

    public ProcessStatus status() {
        return processStatus;
    }

    @SuppressWarnings("unchecked")
    public <T extends NodeDefinition> T readyNode() {
        return (T) readyQueue.poll();
    }

    public void running() {
        processStatus = ProcessStatus.RUNNING;
    }

    public void complete() {
        processStatus = ProcessStatus.COMPLETED;
        EventStream.getInstance().publish(new ProcessExecuteCompletedEvent(this));
    }

    public void completeNode(NodeDefinition currentNode) {
        executedNodes.add(currentNode.refName());
        EventStream.getInstance().publish(new NodeExecuteCompletedEvent(this));
    }

    public <T extends NodeDefinition> void addReadyNode(T next) {
        readyQueue.offer(next);
    }

    public boolean hasException() {
        return Objects.nonNull(executeException);
    }

    public Throwable executeException() {
        return executeException;
    }

    public List<String> executedNodes() {
        return executedNodes.stream().collect(Collectors.toList());
    }

    public Variables input() {
        return Variables.copy(input);
    }

    public void input(Variables params) {
        input.merge(params);
    }

    public void falied(Throwable e) {
        executeException = e;
        processStatus = ProcessStatus.FAILED;
        EventStream.getInstance().publish(new ProcessExecuteFailedEvent(this));
    }
}
