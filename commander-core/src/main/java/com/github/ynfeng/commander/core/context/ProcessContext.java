package com.github.ynfeng.commander.core.context;


import com.github.ynfeng.commander.core.Parameters;
import com.github.ynfeng.commander.core.context.event.EngineEventSubject;
import com.github.ynfeng.commander.core.definition.NodeDefinition;
import com.github.ynfeng.commander.core.definition.ProcessDefinition;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

public class ProcessContext {
    private final ProcessId processId;
    private final ProcessDefinition processDefinition;
    private final ConcurrentLinkedQueue<NodeDefinition> readyQueue = new ConcurrentLinkedQueue<NodeDefinition>();
    private final ConcurrentLinkedQueue<String> executedNodes = new ConcurrentLinkedQueue<String>();
    private final Parameters input = new Parameters();
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
        EngineEventSubject.getInstance().notifyProcessExecutedComplete(this);
    }

    public void completeNode(NodeDefinition currentNode) {
        executedNodes.add(currentNode.refName());
        EngineEventSubject.getInstance().notifyNodeExecutedComplete(this);
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

    public Parameters input() {
        return Parameters.copy(input);
    }

    public void input(Parameters params) {
        input.merge(params);
    }

    public void falied(Throwable e) {
        executeException = e;
        processStatus = ProcessStatus.FAILED;
        EngineEventSubject.getInstance().notifyProcessExecutedException(this);
    }
}
