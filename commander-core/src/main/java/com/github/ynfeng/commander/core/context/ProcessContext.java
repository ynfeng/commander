package com.github.ynfeng.commander.core.context;


import com.github.ynfeng.commander.core.definition.NodeDefinition;
import com.github.ynfeng.commander.core.definition.ProcessDefinition;
import java.util.Objects;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ProcessContext {
    private final ProcessId processId;
    private final ProcessDefinition processDefinition;
    private volatile ProcessStatus processStatus;
    private final ConcurrentLinkedQueue<NodeDefinition> readyQueue = new ConcurrentLinkedQueue<NodeDefinition>();
    private Throwable executeException;
    private static final ThreadLocal<ProcessContext> CONTEXT_HOLDER = new ThreadLocal<>();

    private ProcessContext(ProcessId processId, ProcessDefinition processDefinition) {
        this.processId = processId;
        this.processDefinition = processDefinition;
        processStatus = ProcessStatus.CREATED;
        readyQueue.offer(processDefinition.firstNode());
    }

    public static ProcessContext create(ProcessId processId, ProcessDefinition processDefinition) {
        ProcessContext processContext = new ProcessContext(processId, processDefinition);
        CONTEXT_HOLDER.set(processContext);
        return processContext;
    }

    public static ProcessContext get() {
        return CONTEXT_HOLDER.get();
    }

    public static void threadPropagation(ProcessContext processContext) {
        CONTEXT_HOLDER.set(processContext);
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

    public void running() {
        processStatus = ProcessStatus.RUNNING;
    }

    public void complete() {
        processStatus = ProcessStatus.COMPLETED;
        EngineEventSubject.getInstance().notifyProcessExecutedComplete(this);
    }

    public void completeNode(NodeDefinition next) {
        addReadyNode(next);
        EngineEventSubject.getInstance().notifyNodeExecutedComplete(this);
    }

    private <T extends NodeDefinition> void addReadyNode(T next) {
        readyQueue.offer(next);
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
