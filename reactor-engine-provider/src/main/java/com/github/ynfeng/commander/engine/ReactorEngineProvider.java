package com.github.ynfeng.commander.engine;

import com.github.ynfeng.commander.definition.ProcessDefinitionRepository;
import com.github.ynfeng.commander.support.env.Environment;

public class ReactorEngineProvider implements EngineProvider {

    @Override
    public ProcessEngine getEngine(EngineEnvironment environment, ProcessDefinitionRepository definitionRepository) {
        return new ReactorProcessEngine(environment, definitionRepository);
    }

    @Override
    public Environment prepareEnvironment() {
        return new ReactorEngineEnv(null);
    }
}
