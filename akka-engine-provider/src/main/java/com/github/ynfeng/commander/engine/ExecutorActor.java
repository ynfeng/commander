package com.github.ynfeng.commander.engine;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import com.github.ynfeng.commander.engine.command.CloseExecutorActor;
import com.github.ynfeng.commander.engine.command.ContineExecute;
import com.github.ynfeng.commander.engine.command.EngineCommand;
import com.github.ynfeng.commander.engine.command.ExecuteNode;
import com.github.ynfeng.commander.engine.command.GetNodeExecutingVariable;
import com.github.ynfeng.commander.engine.command.SetNodeExecutingVariable;
import com.github.ynfeng.commander.engine.executor.NodeExecutor;

public class ExecutorActor extends AbstractBehavior<EngineCommand> {
    private final ProcessInstance processInstance;
    private final NodeExecutor executor;
    private final UpdatebleNodeExecutingVariable variable = new UpdatebleNodeExecutingVariable();

    private ExecutorActor(ActorContext<EngineCommand> context,
                          ProcessInstance processInstance,
                          NodeExecutor executor) {
        super(context);
        this.processInstance = processInstance;
        this.executor = executor;
    }

    public static Behavior<EngineCommand> create(ProcessInstance processInstance,
                                                 NodeExecutor executor) {
        return Behaviors.setup(
            ctx -> new ExecutorActor(ctx, processInstance, executor));
    }

    @Override
    public Receive<EngineCommand> createReceive() {
        return newReceiveBuilder()
            .onMessage(ExecuteNode.class, this::onExecuteNode)
            .onMessage(CloseExecutorActor.class, this::onClose)
            .onMessage(GetNodeExecutingVariable.class, this::onGetNodeExecutingVariable)
            .onMessage(SetNodeExecutingVariable.class, this::onSetNodeExecutingVariable)
            .onMessage(ContineExecute.class, this::onContinueExecute)
            .build();
    }

    private Behavior<EngineCommand> onContinueExecute(ContineExecute cmd) {
        executor.execute(processInstance, cmd.nodeDefinition());
        return this;
    }

    private Behavior<EngineCommand> onSetNodeExecutingVariable(SetNodeExecutingVariable cmd) {
        variable.put(cmd.key(), cmd.value());
        cmd.future().complete(variable);
        return this;
    }

    private Behavior<EngineCommand> onGetNodeExecutingVariable(GetNodeExecutingVariable cmd) {
        cmd.future().complete(variable);
        return this;
    }

    private Behavior<EngineCommand> onClose(CloseExecutorActor cmd) {
        return Behaviors.stopped();
    }

    private Behavior<EngineCommand> onExecuteNode(ExecuteNode executeNode) {
        executor.execute(processInstance, executeNode.nodeDefinition());
        return this;
    }
}
