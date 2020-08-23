package com.github.ynfeng.commander.definition;

import java.util.Optional;

public interface ProcessDefinitionRepository {
    Optional<ProcessDefinition> findProcessDefinition(String name, int version);
}
