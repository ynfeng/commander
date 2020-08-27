package com.github.ynfeng.commander.engine;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import com.github.ynfeng.commander.definition.NodeDefinition;
import com.github.ynfeng.commander.engine.command.ContinueNodeExecute;
import com.github.ynfeng.commander.engine.command.ContinueProcess;
import com.github.ynfeng.commander.engine.command.EngineCommand;
import com.github.ynfeng.commander.engine.command.ProcessInstanceStart;
import com.github.ynfeng.commander.engine.command.StartProcess;
import java.util.Optional;

public class EngineActor extends AbstractBehavior<EngineCommand> {
    private final ProcessIdGenerator idGenerator;
    private final EngineEnvironment environment;
    private final ProcessInstanceActorRefs instanceActorRefs = new ProcessInstanceActorRefs();

    public EngineActor(ActorContext<EngineCommand> context, EngineEnvironment environment) {
        super(context);
        idGenerator = environment.getProcessIdGenerator();
        this.environment = environment;
    }

    public static Behavior<EngineCommand> create(EngineEnvironment environment) {
        return Behaviors.setup(ctx -> new EngineActor(ctx, environment));
    }

    @Override
    public Receive<EngineCommand> createReceive() {
        return newReceiveBuilder()
            .onMessage(StartProcess.class, this::onStartProcess)
            .onMessage(ContinueProcess.class, this::onContinueProcess)
            .build();
    }

    private Behavior<EngineCommand> onContinueProcess(ContinueProcess cmd) {
        Optional<ActorRef<EngineCommand>> refOptional = instanceActorRefs.get(cmd.processId());
        if (refOptional.isPresent()) {
            continueProcess(cmd, refOptional);
        } else {
            completeExceptionally(cmd);
        }
        return this;
    }

    private void continueProcess(ContinueProcess cmd, Optional<ActorRef<EngineCommand>> refOptional) {
        ActorRef<EngineCommand> ref = refOptional.get();
        ref.tell(new ContinueNodeExecute(cmd.nodeRefName(), cmd.variables(), cmd.processFuture()));
    }

    private void completeExceptionally(ContinueProcess cmd) {
        cmd.processFuture()
            .completeExceptionally(new ProcessEngineException(
                String.format("No such process instance [%s]", cmd.processId())));
    }

    private Behavior<EngineCommand> onStartProcess(StartProcess cmd) {
        ProcessId processId = idGenerator.nextId();
        String name = String.format("process-%s", processId);
        Behavior<EngineCommand> behavior = buildProcessInstanceActor(cmd, processId);
        ActorRef<EngineCommand> processInstanceRef = getContext().spawn(behavior, name);
        instanceActorRefs.add(processId, processInstanceRef);
        NodeDefinition firstNode = cmd.processDefinition().firstNode();
        processInstanceRef.tell(new ProcessInstanceStart(firstNode));
        return this;
    }

    private Behavior<EngineCommand> buildProcessInstanceActor(StartProcess cmd, ProcessId processId) {
        return Behaviors.setup(ctx -> {
            ProcessInstanceActorBuilder builder = ProcessInstanceActor.builder(ctx)
                .environment(environment)
                .processId(processId)
                .variables(cmd.variables())
                .processFuture(cmd.processFuture());
            return builder.build();
        });
    }
}
