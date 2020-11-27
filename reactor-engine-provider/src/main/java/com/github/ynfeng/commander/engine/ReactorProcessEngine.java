package com.github.ynfeng.commander.engine;

import com.github.ynfeng.commander.definition.ProcessDefinition;
import com.github.ynfeng.commander.definition.ProcessDefinitionRepository;
import com.github.ynfeng.commander.support.logger.CmderLogger;
import com.github.ynfeng.commander.support.logger.CmderLoggerFactory;
import java.util.Optional;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;
import reactor.core.scheduler.Schedulers;

public class ReactorProcessEngine implements ProcessEngine {
    private static final CmderLogger LOGGER = CmderLoggerFactory.getSystemLogger();
    private final EngineEnvironment environment;
    private final ProcessDefinitionRepository definitionRepository;
    private final EngineContext context = new EngineContext();
    private Sinks.Many<EngineCommand> cmdSinks;

    public ReactorProcessEngine(EngineEnvironment environment,
                                ProcessDefinitionRepository definitionRepository) {
        this.environment = environment;
        this.definitionRepository = definitionRepository;
    }

    @Override
    public void startup() {
        cmdSinks = Sinks.many().unicast().onBackpressureBuffer();
        cmdSinks.asFlux()
            .publishOn(Schedulers.newParallel("process acceptor"))
            .subscribe(EngineCommand::execute);
    }

    @Override
    public ProcessFuture startProcess(String name, int version, Variables variables) {
        ProcessFuture future = new ProcessFuture();
        if (name == null) {
            future.completeExceptionally(new NullPointerException(
                "process definition name is required."));
        }
        cmdSinks.emitNext(() -> startNewProcess(name, version, variables, future),
            Sinks.EmitFailureHandler.FAIL_FAST);
        return future;
    }

    @SuppressWarnings("checkstyle:ParameterNumber")
    private void startNewProcess(String name, int version, Variables variables, ProcessFuture future) {
        LOGGER.debug("start new process[{},{}] ,variables:{}", name, version, variables);
        Mono.justOrEmpty(name)
            .map(defName -> loadProcessDefinition(defName, version))
            .map(def -> newProcessInstance(def, variables, future))
            .doOnError(error -> future.completeExceptionally(error))
            .doOnNext(instance -> saveInstanceToContext(instance))
            .subscribe(instance -> instance.run());
    }

    private void saveInstanceToContext(ReactorProcessInstance instance) {
        context.addProcessInstance(instance);
    }

    private ReactorProcessInstance newProcessInstance(ProcessDefinition processDefinition,
                                                      Variables variables,
                                                      ProcessFuture future) {
        return ReactorProcessInstance.builder()
            .environment(environment)
            .processFuture(future)
            .variables(variables)
            .processDefinition(processDefinition)
            .processId(environment.getProcessIdGenerator().nextId())
            .build();
    }

    private ProcessDefinition loadProcessDefinition(String name, int version) {
        return definitionRepository.findProcessDefinition(name, version)
            .orElseThrow(() -> new ProcessEngineException("process definition was not exists."));
    }

    @Override
    public ProcessFuture startProcess(String name, int version) {
        return startProcess(name, version, Variables.EMPTY);
    }

    @Override
    public void shutdown() {
        cmdSinks.emitComplete(Sinks.EmitFailureHandler.FAIL_FAST);
    }

    @SuppressWarnings("checkstyle:MethodLength")
    @Override
    public ContinueFuture continueProcess(ProcessId processId, String nodeRefName, Variables variables) {
        ContinueFuture continueFuture = new ContinueFuture();
        cmdSinks.emitNext(() -> {
            Optional<ReactorProcessInstance> processInstance = context.getProcessInstance(processId);
            ProcessFuture future = null;
            if (processInstance.isPresent()) {
                future = processInstance.get().continueRunningNode(nodeRefName, variables);
                continueFuture.complete(future);
            } else {
                continueFuture.completeExceptionally(
                    new ProcessEngineException("no such process instance to contine."));
            }
        }, Sinks.EmitFailureHandler.FAIL_FAST);
        return continueFuture;
    }

}
