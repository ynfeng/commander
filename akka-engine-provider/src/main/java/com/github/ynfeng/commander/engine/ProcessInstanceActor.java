package com.github.ynfeng.commander.engine;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import com.github.ynfeng.commander.definition.NodeDefinition;
import com.github.ynfeng.commander.engine.command.EngineCommand;
import com.github.ynfeng.commander.engine.command.ProcessComplete;
import com.github.ynfeng.commander.engine.command.ProcessInstanceStart;
import com.github.ynfeng.commander.engine.command.RunNodes;
import com.github.ynfeng.commander.engine.executor.NodeExecutor;
import com.github.ynfeng.commander.engine.executor.NodeExecutors;
import com.github.ynfeng.commander.engine.executor.SPINodeExecutors;
import com.google.common.collect.Lists;
import java.util.Queue;

public class ProcessInstanceActor extends AbstractBehavior<EngineCommand> implements ProcessInstance {
    private final ProcessId processId;
    private final ProcessFuture processFuture;
    private Queue<NodeDefinition> readyNodes = Lists.newLinkedList();
    private final NodeExecutors nodeExecutors = new SPINodeExecutors();

    public ProcessInstanceActor(ActorContext<EngineCommand> context,
                                ProcessId processId,
                                ProcessFuture processFuture) {
        super(context);
        this.processId = processId;
        this.processFuture = processFuture;
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
        getContext().getSelf().tell(new RunNodes());
    }

    @Override
    public Receive<EngineCommand> createReceive() {
        return newReceiveBuilder()
            .onMessage(ProcessInstanceStart.class, this::onStart)
            .onMessage(RunNodes.class, this::onRunNodes)
            .onMessage(ProcessComplete.class, this::onComplete)
            .onMessage(AddReadyNode.class, this::onAddReadyNode)
            .build();
    }

    private Behavior<EngineCommand> onAddReadyNode(AddReadyNode cmd) {
        readyNodes.add(cmd.nextNode());
        return this;
    }

    private Behavior<EngineCommand> onComplete(ProcessComplete cmd) {
        processFuture.complete(null);
        return Behaviors.stopped();
    }

    private Behavior<EngineCommand> onRunNodes(RunNodes cmd) {
        NodeDefinition nextNode = readyNodes.poll();
        while (nextNode != NodeDefinition.NULL && null != nextNode) {
            NodeExecutor executor = nodeExecutors.getExecutor(nextNode);
            executor.execute(this, nextNode);
            nextNode = readyNodes.poll();
        }
        return this;
    }

    private Behavior<EngineCommand> onStart(ProcessInstanceStart cmd) {
        getContext().getSelf().tell(new AddReadyNode(cmd.firstNode()));
        getContext().getSelf().tell(new RunNodes());
        return this;
    }

    public static Behavior<EngineCommand> create(ProcessId processId, ProcessFuture processFuture) {
        return Behaviors.setup(ctx -> new ProcessInstanceActor(ctx, processId, processFuture));
    }
}
