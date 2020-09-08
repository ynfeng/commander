package com.github.ynfeng.commander.engine;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.Terminated;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import com.github.ynfeng.commander.definition.NodeDefinition;
import com.github.ynfeng.commander.engine.command.CloseExecutorActor;
import com.github.ynfeng.commander.engine.command.ContineExecute;
import com.github.ynfeng.commander.engine.command.ContinueNodeExecute;
import com.github.ynfeng.commander.engine.command.EngineCommand;
import com.github.ynfeng.commander.engine.command.ExecuteNode;
import com.github.ynfeng.commander.engine.command.GetExecutedNodes;
import com.github.ynfeng.commander.engine.command.GetExecutedNodesResponse;
import com.github.ynfeng.commander.engine.command.GetExecutingVariableSuccess;
import com.github.ynfeng.commander.engine.command.GetNodeExecutingVariable;
import com.github.ynfeng.commander.engine.command.GetNodeExecutingVariableResponse;
import com.github.ynfeng.commander.engine.command.NodeComplete;
import com.github.ynfeng.commander.engine.command.ProcessComplete;
import com.github.ynfeng.commander.engine.command.ProcessInstanceStart;
import com.github.ynfeng.commander.engine.command.RunNodes;
import com.github.ynfeng.commander.engine.command.SetNodeExecutingVariable;
import com.github.ynfeng.commander.engine.command.SetNodeExecutingVariableResponse;
import com.github.ynfeng.commander.engine.executor.NodeExecutingVariable;
import com.github.ynfeng.commander.engine.executor.NodeExecutors;
import com.github.ynfeng.commander.support.logger.CmderLogger;
import com.github.ynfeng.commander.support.logger.CmderLoggerFactory;
import com.google.common.collect.Lists;
import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class ProcessInstanceActor extends AbstractBehavior<EngineCommand> implements ProcessInstance {
    private static final CmderLogger LOGGER = CmderLoggerFactory.getSystemLogger();
    private ProcessId processId;
    private ProcessFuture processFuture;
    private EngineEnvironment environment;
    private final Variables input = new Variables();
    private final ExecutorActors executorActors = new ExecutorActors();
    private final ReadyNodes readyNodes = new ReadyNodes();
    private final RunningNodes runningNodes = new RunningNodes();
    private final List<NodeDefinition> executedNodes = Lists.newArrayList();

    protected ProcessInstanceActor(ActorContext<EngineCommand> context) {
        super(context);
    }

    @Override
    public void start() {

    }

    @Override
    public void addReadyNode(NodeDefinition nodeDefinition) {
        getContext().getSelf().tell(new AddReadyNode(nodeDefinition));
    }

    @Override
    public void processComplete() {
        getContext().getSelf().tell(new ProcessComplete());
    }

    @Override
    public CompletableFuture<NodeExecutingVariable> getNodeExecutingVariable(String refName) {
        ActorRef<EngineCommand> executorRef = executorActors.get(refName);
        CompletableFuture<NodeExecutingVariable> future = new CompletableFuture<>();
        doGetNodeExecutingVariable(executorRef, future);
        return future;
    }

    private void doGetNodeExecutingVariable(ActorRef<EngineCommand> executorRef,
                                            CompletableFuture<NodeExecutingVariable> future) {
        getContext().ask(
            GetNodeExecutingVariableResponse.class,
            executorRef,
            Duration.ofSeconds(5),
            ref -> new GetNodeExecutingVariable(ref),
            (response, throwable) -> new GetExecutingVariableSuccess(future, response.variable()));
    }

    @Override
    public CompletableFuture<NodeExecutingVariable> setNodeExecutingVariable(String refName, String key, Object val) {
        ActorRef<EngineCommand> executorRef = executorActors.get(refName);
        return doSetNodeExecutingVariable(key, val, executorRef);
    }

    @Override
    public CompletableFuture<List<String>> executedNodes() {
        CompletableFuture<List<String>> future = new CompletableFuture<>();
        doGetExecuteNodes(future);
        return future;
    }

    @SuppressWarnings("checkstyle:MethodLength")
    private void doGetExecuteNodes(CompletableFuture<List<String>> future) {
        getContext().ask(
            GetExecutedNodesResponse.class,
            getContext().getSelf(),
            Duration.ofSeconds(5),
            ref -> new GetExecutedNodes(ref),
            (response, throwable) -> {
                future.complete(response.executedNodes());
                return null;
            });
    }

    private CompletableFuture<NodeExecutingVariable> doSetNodeExecutingVariable(String key,
                                                                                Object val,
                                                                                ActorRef<EngineCommand> executorRef) {
        CompletableFuture<NodeExecutingVariable> future = new CompletableFuture<>();
        getContext().ask(
            SetNodeExecutingVariableResponse.class,
            executorRef,
            Duration.ofSeconds(5),
            ref -> new SetNodeExecutingVariable(key, val, ref),
            (response, throwable) -> new SetExecutingVariableSuccess(future, response.variable()));
        return future;
    }

    @Override
    public void nodeComplete(NodeDefinition nodeDefinition) {
        getContext().getSelf().tell(new NodeComplete(nodeDefinition));
        getContext().getSelf().tell(new RunNodes());
    }

    @SuppressWarnings("checkstyle:MethodLength")
    @Override
    public Receive<EngineCommand> createReceive() {
        return newReceiveBuilder()
            .onMessage(ProcessInstanceStart.class, this::onProcessInstanceStart)
            .onMessage(RunNodes.class, this::onRunNodes)
            .onMessage(ProcessComplete.class, this::onComplete)
            .onMessage(NodeComplete.class, this::onNodeComplete)
            .onMessage(AddReadyNode.class, this::onAddReadyNode)
            .onMessage(ContinueNodeExecute.class, this::onContinueNodeExecute)
            .onMessage(GetExecutingVariableSuccess.class, this::onGetExecutingVariableSuccess)
            .onMessage(SetExecutingVariableSuccess.class, this::onSetExecutingVariableSuccess)
            .onMessage(GetExecutedNodes.class, this::onGetExecutedNodes)
//            .onSignal(ChildFailed.class, this::onChildFailed)
            .onSignal(Terminated.class, this::onTerminated)
            .build();
    }

    private Behavior<EngineCommand> onGetExecutedNodes(GetExecutedNodes cmd) {
        cmd.replyTo().tell(new GetExecutedNodesResponse(Lists.newArrayList(executedNodes)));
        return this;
    }

    private Behavior<EngineCommand> onSetExecutingVariableSuccess(SetExecutingVariableSuccess cmd) {
        cmd.future().complete(cmd.variable());
        return this;
    }

    private Behavior<EngineCommand> onGetExecutingVariableSuccess(GetExecutingVariableSuccess cmd) {
        cmd.future().complete(cmd.variable());
        return this;
    }

    private Behavior<EngineCommand> onTerminated(Terminated terminated) {
        LOGGER.debug(String.format("Executor actor %s terminated.",
            terminated.getRef().path()));
        return this;
    }

//    private Behavior<EngineCommand> onChildFailed(ChildFailed childFailed) {
//        LOGGER.debug(String.format("Executor actor %s failed.", childFailed.getRef().path()));
//        processFuture.completeExceptionally(childFailed.cause());
//        return this;
//    }

    private Behavior<EngineCommand> onContinueNodeExecute(ContinueNodeExecute cmd) {
        Optional<NodeDefinition> nodeDefinitionOptional = runningNodes.get(cmd.nodeRefName());
        if (nodeDefinitionOptional.isPresent()) {
            continueExecute(cmd, nodeDefinitionOptional);
        } else {
            completeExceptionally(cmd);
        }
        return this;
    }

    private static void completeExceptionally(ContinueNodeExecute cmd) {
        cmd.processFuture()
            .completeExceptionally(new ProcessEngineException(
                String.format("No such executing node[%s]", cmd.nodeRefName())));
    }

    private void continueExecute(ContinueNodeExecute cmd, Optional<NodeDefinition> nodeDefinitionOptional) {
        processFuture = cmd.processFuture();
        ActorRef<EngineCommand> ref = executorActors.get(cmd.nodeRefName());
        ref.tell(new ContineExecute(nodeDefinitionOptional.get(), cmd.variables()));
    }

    private Behavior<EngineCommand> onNodeComplete(NodeComplete cmd) {
        NodeDefinition nodeDefinition = cmd.nodeDefinition();
        ActorRef<EngineCommand> executorActorRef = executorActors.remove(nodeDefinition.refName());
        if (Objects.nonNull(executorActorRef)) {
            runningNodes.remove(cmd.nodeDefinition().refName());
            executedNodes.add(nodeDefinition);
            executorActorRef.tell(new CloseExecutorActor());
        }
        return this;
    }

    private Behavior<EngineCommand> onAddReadyNode(AddReadyNode cmd) {
        readyNodes.add(cmd.nextNode());
        return this;
    }

    private Behavior<EngineCommand> onComplete(ProcessComplete cmd) {
        ProcessInstanceInfo processInstanceInfo = new ProcessInstanceInfo(
            Collections.unmodifiableList(executedNodes));
        processFuture.complete(processInstanceInfo);
        return Behaviors.stopped();
    }

    private Behavior<EngineCommand> onRunNodes(RunNodes cmd) {
        NodeDefinition nextNode = readyNodes.poll();
        while (nextNode != null) {
            if (canExecute(nextNode)) {
                executeWithActor(nextNode);
            }
            nextNode = readyNodes.poll();
        }
        return this;
    }

    private static boolean canExecute(NodeDefinition nextNode) {
        return nextNode != NodeDefinition.NULL;
    }

    private void executeWithActor(NodeDefinition nextNode) {
        ActorRef<EngineCommand> executorRef = getOrCreateActor(nextNode);
        LOGGER.debug(String.format("Execute node %s with actor %s", nextNode.refName(), executorRef.path()));
        executorRef.tell(new ExecuteNode(nextNode));
    }

    private ActorRef<EngineCommand> getOrCreateActor(NodeDefinition nextNode) {
        ActorRef<EngineCommand> ref = executorActors.get(nextNode.refName());
        if (Objects.isNull(ref)) {
            ref = createActor(nextNode);
            getContext().watch(ref);
        }
        return ref;
    }

    private ActorRef<EngineCommand> createActor(NodeDefinition nextNode) {
        String actorName = String.format("executor-%s", nextNode.refName());
        Behavior<EngineCommand> executorActor = ExecutorActor.create(this, getNodeExecutors().getExecutor(nextNode));
        ActorRef<EngineCommand> executorRef = getContext().spawn(executorActor, actorName);
        runningNodes.add(nextNode);
        executorActors.add(nextNode.refName(), executorRef);
        LOGGER.debug("Create actor {}", actorName);
        return executorRef;
    }

    private Behavior<EngineCommand> onProcessInstanceStart(ProcessInstanceStart cmd) {
        getContext().getSelf().tell(new AddReadyNode(cmd.firstNode()));
        getContext().getSelf().tell(new RunNodes());
        return this;
    }

    public static ProcessInstanceActorBuilder builder(ActorContext<EngineCommand> context) {
        return new ProcessInstanceActorBuilder(new ProcessInstanceActor(context));
    }

    protected void setProcessId(ProcessId processId) {
        this.processId = processId;
    }

    protected void setInput(Variables variables) {
        input.merge(variables);
    }

    public void setProcessFuture(ProcessFuture processFuture) {
        this.processFuture = processFuture;
    }

    public void setEnvironment(EngineEnvironment environment) {
        this.environment = environment;
    }

    private NodeExecutors getNodeExecutors() {
        return environment.getNodeExecutors();
    }

    public Variables getInput() {
        return input;
    }
}
