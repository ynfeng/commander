package com.github.ynfeng.commander.engine;

import com.github.ynfeng.commander.definition.ProcessDefinitionRepository;
import com.github.ynfeng.commander.support.env.Environment;

public class AkkaEngineProvider implements EngineProvider {

    @Override
    public ProcessEngine getEngine(Environment environment, ProcessDefinitionRepository definitionRepository) {
        return new AkkaProcessEngine(new UUIDProcessIdGenerator(), definitionRepository);
    }

    @Override
    public Environment prepareEnvironment() {
        return new AkkaEngineEnv(null);
    }
}
