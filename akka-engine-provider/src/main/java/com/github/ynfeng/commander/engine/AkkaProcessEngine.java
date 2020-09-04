package com.github.ynfeng.commander.engine;

import akka.actor.typed.ActorSystem;
import com.github.ynfeng.commander.definition.ProcessDefinition;
import com.github.ynfeng.commander.definition.ProcessDefinitionRepository;
import com.github.ynfeng.commander.engine.command.ContinueProcess;
import com.github.ynfeng.commander.engine.command.EngineCommand;
import com.github.ynfeng.commander.engine.command.StartProcess;
import com.google.common.base.Preconditions;
import java.util.Optional;

public class AkkaProcessEngine implements ProcessEngine {
    private EngineEnvironment environment;
    private final ProcessDefinitionRepository definitionRepository;
    private ActorSystem<EngineCommand> engineActor;

    public AkkaProcessEngine(EngineEnvironment environment,
                             ProcessDefinitionRepository definitionRepository) {
        this.environment = environment;
        this.definitionRepository = definitionRepository;
    }

    @Override
    public void startup() {
        engineActor = ActorSystem.create(EngineActor.create(environment), "process-engine");
    }

    @Override
    public ProcessFuture startProcess(String name, int version, Variables variables) {
        Optional<ProcessDefinition> candicate = definitionRepository.findProcessDefinition(
            Preconditions.checkNotNull(name, "process definition name is required."), version);
        ProcessDefinition processDefinition = candicate.orElseThrow(
            () -> new ProcessEngineException("process definition was not exists."));
        ProcessFuture future = new ProcessFuture();
        engineActor.tell(new StartProcess(processDefinition, future, variables));
        return future;
    }

    @Override
    public ProcessFuture startProcess(String name, int version) {
        return startProcess(name, version, Variables.EMPTY);
    }

    @Override
    public void shutdown() {
        engineActor.terminate();
    }

    @Override
    public ProcessFuture continueProcess(ProcessId processId, String nodeRefName, Variables variables) {
        ContinueProcess continueProcess = new ContinueProcess(processId, nodeRefName, variables);
        engineActor.tell(continueProcess);
        return continueProcess.processFuture();
    }
}
