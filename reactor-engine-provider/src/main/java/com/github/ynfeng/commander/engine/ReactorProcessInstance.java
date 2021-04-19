package com.github.ynfeng.commander.engine;

import com.github.ynfeng.commander.definition.NodeDefinition;
import com.github.ynfeng.commander.definition.ProcessDefinition;
import com.github.ynfeng.commander.engine.executor.NodeExecutingVariable;
import com.github.ynfeng.commander.engine.executor.NodeExecutors;
import com.github.ynfeng.commander.support.logger.CmderLogger;
import com.github.ynfeng.commander.support.logger.CmderLoggerFactory;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import reactor.core.publisher.Sinks;

public class ReactorProcessInstance implements ProcessInstance {
    private static final CmderLogger LOGGER = CmderLoggerFactory.getSystemLogger();
    private final Sinks.Many<EngineCommand> commandSinks;
    private final Variables input = new Variables();
    private final ProcessInstanceRuntimeContext context = new ProcessInstanceRuntimeContext();
    private ProcessId processId;
    private ProcessFuture future;
    private ProcessDefinition processDefinition;
    private NodeExecutors nodeExecutors;

    protected ReactorProcessInstance() {
        commandSinks = Sinks.many().unicast().onBackpressureBuffer();
        commandSinks.asFlux()
            .doOnNext(EngineCommand::execute)
            .doOnComplete(this::assemblyResult)
            .doOnError(this::assemblyException)
            .subscribe();
    }

    public static ProcessInstanceBuilder builder() {
        return new ProcessInstanceBuilder();
    }

    private void assemblyException(Throwable error) {
        future.notifyProcessCompleteExceptionally(error);
    }

    private void assemblyResult() {
        ProcessResult.ProcessResultBuilder builder = ProcessResult.builder()
            .executedNodes(context.getExecuteNodeRefNames());
        future.notifyProcessComplete(builder.build());
    }

    @Override
    public void start() {
    }

    @Override
    public void addReadyNode(NodeDefinition nodeDefinition) {
        commandSinks.emitNext(() -> {
            context.addReadyNode(nodeDefinition);
            future.makeNotifyCondition(nodeDefinition.refName());
        }, Sinks.EmitFailureHandler.FAIL_FAST);
    }

    @Override
    public void nodeComplete(NodeDefinition nodeDefinition) {
        LOGGER.debug("node completed process[{}] node[{}]", processId, nodeDefinition.refName());
        removeRunningNode(nodeDefinition);
        addExecutedNode(nodeDefinition);
        notifyNodeComplete(nodeDefinition);
        runReadyNodes();
    }

    private void notifyNodeComplete(NodeDefinition nodeDefinition) {
        commandSinks.emitNext(() -> {
            future.notifyNodeComplete(nodeDefinition.refName());
        }, Sinks.EmitFailureHandler.FAIL_FAST);
    }

    private void addExecutedNode(NodeDefinition nodeDefinition) {
        commandSinks.emitNext(() -> context.addExecutedNode(nodeDefinition), Sinks.EmitFailureHandler.FAIL_FAST);
    }

    private void removeRunningNode(NodeDefinition nodeDefinition) {
        commandSinks.emitNext(() -> context.removeRunningNode(nodeDefinition.refName()),
            Sinks.EmitFailureHandler.FAIL_FAST);
    }

    private void runReadyNodes() {
        commandSinks.emitNext(() -> {
            NodeDefinition nextNode = context.nextReadyNode();
            while (nextNode != null) {
                LOGGER.debug("ready to run process[{}] node[{}]", processId, nextNode.refName());
                context.addRunningNode(nextNode);
                nodeExecutors.getExecutor(nextNode)
                    .execute(this, nextNode);
                future.notifyNodeStart(nextNode.refName());
                nextNode = context.nextReadyNode();
            }
        }, Sinks.EmitFailureHandler.FAIL_FAST);
    }

    @Override
    public void processComplete() {
        LOGGER.debug("process[{}] completed", processId);
        commandSinks.emitComplete(Sinks.EmitFailureHandler.FAIL_FAST);
    }

    @Override
    public CompletableFuture<NodeExecutingVariable> getNodeExecutingVariable(String refName) {
        CompletableFuture<NodeExecutingVariable> future = new CompletableFuture<NodeExecutingVariable>();
        commandSinks.emitNext(() -> {
            future.complete(context.getNodeExecutingVariable(refName));
        }, Sinks.EmitFailureHandler.FAIL_FAST);
        return future;
    }

    @Override
    public CompletableFuture<NodeExecutingVariable> setNodeExecutingVariable(String refName, String key, Object val) {
        CompletableFuture<NodeExecutingVariable> future = new CompletableFuture<NodeExecutingVariable>();
        commandSinks.emitNext(() -> {
            context.setNodeExecutingVariable(refName, key, val);
            future.complete(context.getNodeExecutingVariable(refName));
        }, Sinks.EmitFailureHandler.FAIL_FAST);
        return future;
    }

    @Override
    public CompletableFuture<List<String>> executedNodes() {
        CompletableFuture<List<String>> future = new CompletableFuture<>();
        commandSinks.emitNext(() -> {
            List<String> refNameList = context.getExecuteNodeRefNames();
            future.complete(refNameList);
        }, Sinks.EmitFailureHandler.FAIL_FAST);
        return future;
    }

    public void run() {
        addReadyNode(processDefinition.firstNode());
        runReadyNodes();
        future.notifyProcessStarted(processId);
    }

    public ProcessFuture continueRunningNode(String refName, Variables variables) {
        commandSinks.emitNext(() -> {
            LOGGER.debug("continue to run process[{}] node[{}]", processId, refName);
            NodeDefinition node = context.getRunningNode(refName)
                .orElseThrow(() -> new ProcessEngineException(
                    String.format("No such executing node[%s]", refName)));
            input.merge(variables);
            context.addReadyNode(node);
        }, Sinks.EmitFailureHandler.FAIL_FAST);
        runReadyNodes();
        return future;
    }

    protected ProcessId processId() {
        return processId;
    }

    protected void setProcessId(ProcessId processId) {
        this.processId = processId;
    }

    protected void setProcessFuture(ProcessFuture processFuture) {
        future = processFuture;
    }

    protected void setNodeExecutors(NodeExecutors nodeExecutors) {
        this.nodeExecutors = nodeExecutors;
    }

    protected void setProcessDefinition(ProcessDefinition processDefinition) {
        this.processDefinition = processDefinition;
    }

    public Variables getInput() {
        return input;
    }

    protected void setInput(Variables variables) {
        input.merge(variables);
    }
}
