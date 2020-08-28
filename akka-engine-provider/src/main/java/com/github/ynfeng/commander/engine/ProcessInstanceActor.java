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
import com.github.ynfeng.commander.engine.command.GetExecutingVariableSuccess;
import com.github.ynfeng.commander.engine.command.GetNodeExecutingVariable;
import com.github.ynfeng.commander.engine.command.NodeComplete;
import com.github.ynfeng.commander.engine.command.NodeExecutingVariableResponse;
import com.github.ynfeng.commander.engine.command.ProcessComplete;
import com.github.ynfeng.commander.engine.command.ProcessInstanceStart;
import com.github.ynfeng.commander.engine.command.RunNodes;
import com.github.ynfeng.commander.engine.command.SetNodeExecutingVariable;
import com.github.ynfeng.commander.engine.executor.NodeExecutingVariable;
import com.github.ynfeng.commander.engine.executor.NodeExecutors;
import com.github.ynfeng.commander.support.logger.CmderLogger;
import com.github.ynfeng.commander.support.logger.CmderLoggerFactory;
import com.google.common.collect.Lists;
import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class ProcessInstanceActor extends AbstractBehavior<EngineCommand> implements ProcessInstance {
    private static final CmderLogger LOGGER = CmderLoggerFactory.getSystemLogger();
    private ProcessId processId;
    private ProcessFuture processFuture;
    private EngineEnvironment environment;
    private final Variables variables = new Variables();
    private final ExecutorActors executorActors = new ExecutorActors();
    private final ReadyNodes readyNodes = new ReadyNodes();
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
            NodeExecutingVariableResponse.class,
            executorRef,
            Duration.ofSeconds(5),
            ref -> new GetNodeExecutingVariable(ref),
            (response, throwable) -> new GetExecutingVariableSuccess(future, response.variable()));
    }

    @Override
    public CompletableFuture<NodeExecutingVariable> setNodeExecutingVariable(String refName, String key, Object val) {
        //TODO ask
        ActorRef<EngineCommand> executorRef = executorActors.get(refName);
        CompletableFuture<NodeExecutingVariable> future = new CompletableFuture<>();
        executorRef.tell(new SetNodeExecutingVariable(key, val, future));
        return future;
    }

    @Override
    public void nodeComplete(NodeDefinition nodeDefinition) {
        getContext().getSelf().tell(new NodeComplete());
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
//            .onSignal(ChildFailed.class, this::onChildFailed)
            .onSignal(Terminated.class, this::onTerminated)
            .build();
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
        Optional<NodeDefinition> nodeDefinitionOptional = readyNodes.get(cmd.nodeRefName());
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
        NodeDefinition nodeDefinition = readyNodes.poll();
        ActorRef<EngineCommand> executorActorRef = executorActors.remove(nodeDefinition.refName());
        executorActorRef.tell(new CloseExecutorActor());
        executedNodes.add(nodeDefinition);
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
        NodeDefinition nextNode = readyNodes.peek();
        if (canExecute(nextNode)) {
            executeWithActor(nextNode);
        }
        return this;
    }

    private static boolean canExecute(NodeDefinition nextNode) {
        return nextNode != NodeDefinition.NULL && null != nextNode;
    }

    private void executeWithActor(NodeDefinition nextNode) {
        Behavior<EngineCommand> executorActor = ExecutorActor.create(this,
            getNodeExecutors().getExecutor(nextNode));
        ActorRef<EngineCommand> executorRef = getContext().spawn(executorActor,
            String.format("executor-%s", nextNode.refName()));
        executorActors.add(nextNode.refName(), executorRef);
        getContext().watch(executorRef);
        LOGGER.debug(String.format("Execute node %s with actor %s", nextNode.refName(), executorRef.path()));
        executorRef.tell(new ExecuteNode(nextNode));
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

    protected void setVariables(Variables variables) {
        this.variables.merge(variables);
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
}
