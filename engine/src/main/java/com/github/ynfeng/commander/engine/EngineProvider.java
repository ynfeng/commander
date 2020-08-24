package com.github.ynfeng.commander.engine;


import com.github.ynfeng.commander.definition.ProcessDefinitionRepository;
import com.github.ynfeng.commander.support.env.Environment;

public interface EngineProvider {
    ProcessEngine getEngine(Environment environment, ProcessDefinitionRepository definitionRepository);

    Environment prepareEnvironment();
}