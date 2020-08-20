package com.github.ynfeng.commander.engine;

import com.github.ynfeng.commander.definition.EndDefinition;
import com.github.ynfeng.commander.definition.ProcessDefinition;
import com.github.ynfeng.commander.definition.RelationShips;
import com.github.ynfeng.commander.definition.StartDefinition;
import org.junit.jupiter.api.Test;

class AkkaProcessEngineTest {

    @Test
    public void should_start_process() {
        ProcessDefinition processDefinition = ProcessDefinition.builder()
            .withVersion(1)
            .withName("fooTest")
            .withNodes(
                new StartDefinition(),
                new EndDefinition("end")
            ).withRelationShips(
                RelationShips.builder()
                    .withLink("start", "end")
                    .build()
            ).build();

        ProcessEngine engine = new AkkaProcessEngine(new UUIDProcessIdGenerator());
        engine.startup();
        engine.startProcess(processDefinition).join();
        engine.shutdown();
    }

}
