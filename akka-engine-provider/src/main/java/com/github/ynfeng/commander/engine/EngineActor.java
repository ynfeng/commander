package com.github.ynfeng.commander.engine;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import com.github.ynfeng.commander.definition.NodeDefinition;
import com.github.ynfeng.commander.engine.command.EngineCommand;
import com.github.ynfeng.commander.engine.command.ProcessInstanceStart;
import com.github.ynfeng.commander.engine.command.StartProcess;

public class EngineActor extends AbstractBehavior<EngineCommand> {
    private final ProcessIdGenerator idGenerator;

    public EngineActor(ActorContext<EngineCommand> context, ProcessIdGenerator idGenerator) {
        super(context);
        this.idGenerator = idGenerator;
    }

    public static Behavior<EngineCommand> create(ProcessIdGenerator idGenerator) {
        return Behaviors.setup(ctx -> new EngineActor(ctx, idGenerator));
    }

    @Override
    public Receive<EngineCommand> createReceive() {
        return newReceiveBuilder()
            .onMessage(StartProcess.class, this::onStartProcess)
            .build();
    }

    private Behavior<EngineCommand> onStartProcess(StartProcess cmd) {
        ProcessId processId = idGenerator.nextId();
        String name = String.format("process-%s", processId);
        Behavior<EngineCommand> behavior = ProcessInstanceActor.create(processId, cmd.variables(), cmd.processFuture());
        ActorRef<EngineCommand> processInstanceRef = getContext().spawn(behavior, name);
        NodeDefinition firstNode = cmd.processDefinition().firstNode();
        processInstanceRef.tell(new ProcessInstanceStart(firstNode));
        return this;
    }
}
