package com.github.ynfeng.commander.engine;

import akka.actor.typed.ActorSystem;
import com.github.ynfeng.commander.definition.ProcessDefinition;

public class AkkaProcessEngine implements ProcessEngine {
    private ActorSystem<EngineCommand> engineActor;
    private final ProcessIdGenerator processIdGenerator;

    public AkkaProcessEngine(ProcessIdGenerator processIdGenerator) {
        this.processIdGenerator = processIdGenerator;
    }

    @Override
    public void startup() {
        engineActor = ActorSystem.create(EngineActor.create(processIdGenerator), "process-engine");
    }

    @Override
    public ProcessFuture startProcess(ProcessDefinition processDefinition) {
        ProcessFuture future = new ProcessFuture();
        engineActor.tell(new StartProcess(processDefinition, future));
        return future;
    }

    @Override
    public void shutdown() {
        engineActor.terminate();
    }
}
