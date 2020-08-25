package com.github.ynfeng.commander.engine;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import com.github.ynfeng.commander.definition.NodeDefinition;
import com.github.ynfeng.commander.engine.command.CloseExecutorActor;
import com.github.ynfeng.commander.engine.command.EngineCommand;
import com.github.ynfeng.commander.engine.command.ExecuteNode;
import com.github.ynfeng.commander.engine.command.NodeComplete;
import com.github.ynfeng.commander.engine.command.ProcessComplete;
import com.github.ynfeng.commander.engine.command.ProcessInstanceStart;
import com.github.ynfeng.commander.engine.command.RunNodes;
import com.github.ynfeng.commander.engine.executor.NodeExecutors;
import com.github.ynfeng.commander.engine.executor.SPINodeExecutors;
import com.google.common.collect.Lists;
import java.util.Collections;
import java.util.List;
import java.util.Queue;

public class ProcessInstanceActor extends AbstractBehavior<EngineCommand> implements ProcessInstance {
    private ProcessId processId;
    private Variables variables;
    private ProcessFuture processFuture;
    private final Queue<NodeDefinition> readyNodes = Lists.newLinkedList();
    private final List<NodeDefinition> executedNodes = Lists.newArrayList();
    private final NodeExecutors nodeExecutors = new SPINodeExecutors();
    private final ExecutorActors executorActors = new ExecutorActors();

    public ProcessInstanceActor(ActorContext<EngineCommand> context) {
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
    public void nodeComplete(NodeDefinition nodeDefinition) {
        getContext().getSelf().tell(new NodeComplete());
        getContext().getSelf().tell(new RunNodes());
    }

    @Override
    public Receive<EngineCommand> createReceive() {
        return newReceiveBuilder()
            .onMessage(ProcessInstanceStart.class, this::onProcessInstanceStart)
            .onMessage(RunNodes.class, this::onRunNodes)
            .onMessage(ProcessComplete.class, this::onComplete)
            .onMessage(NodeComplete.class, this::onNodeComplete)
            .onMessage(AddReadyNode.class, this::onAddReadyNode)
            .build();
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

    private boolean canExecute(NodeDefinition nextNode) {
        return nextNode != NodeDefinition.NULL && null != nextNode;
    }

    private void executeWithActor(NodeDefinition nextNode) {
        String name = String.format("executor-%s", nextNode.refName());
        Behavior<EngineCommand> executorActor = ExecutorActor.create(this,
            nodeExecutors.getExecutor(nextNode));
        ActorRef<EngineCommand> executorRef = getContext().spawn(executorActor, name);
        executorActors.add(nextNode.refName(), executorRef);
        executorRef.tell(new ExecuteNode(nextNode));
    }

    private Behavior<EngineCommand> onProcessInstanceStart(ProcessInstanceStart cmd) {
        getContext().getSelf().tell(new AddReadyNode(cmd.firstNode()));
        getContext().getSelf().tell(new RunNodes());
        return this;
    }

    public static Behavior<EngineCommand> create(ProcessId processId,
                                                 Variables variables,
                                                 ProcessFuture processFuture) {
        return Behaviors.setup(
            ctx -> {
                ProcessInstanceActorBuilder builder = builder(ctx)
                    .processId(processId)
                    .variables(variables)
                    .processFuture(processFuture);
                return builder.build();
            });
    }

    private static ProcessInstanceActorBuilder builder(ActorContext<EngineCommand> context) {
        return new ProcessInstanceActorBuilder(new ProcessInstanceActor(context));
    }

    protected void setProcessId(ProcessId processId) {
        this.processId = processId;
    }

    protected void setVariables(Variables variables) {
        this.variables = variables;
    }

    public void setProcessFuture(ProcessFuture processFuture) {
        this.processFuture = processFuture;
    }
}
