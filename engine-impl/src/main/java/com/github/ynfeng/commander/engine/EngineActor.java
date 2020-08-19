package com.github.ynfeng.commander.engine;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;

public class EngineActor extends AbstractBehavior<EngineCommand> {

    public EngineActor(ActorContext<EngineCommand> context) {
        super(context);
    }

    public static Behavior<EngineCommand> create() {
        return Behaviors.setup(EngineActor::new);
    }


    @Override
    public Receive<EngineCommand> createReceive() {
        return newReceiveBuilder().onMessage(StartProcess.class, this::onStartProcess).build();
    }

    private Behavior<EngineCommand> onStartProcess(StartProcess cmd) {
        System.out.println("start process.");
        cmd.processDefinition();
        return this;
    }
}
