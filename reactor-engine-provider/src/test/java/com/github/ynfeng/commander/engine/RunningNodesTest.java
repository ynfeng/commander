package com.github.ynfeng.commander.engine;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.github.ynfeng.commander.definition.NodeDefinition;
import org.junit.jupiter.api.Test;

class RunningNodesTest {
    
    @Test
    public void should_not_add_null_and_null_NULL_definition(){
        RunningNodes runningNodes = new RunningNodes();
        runningNodes.add(null);
        runningNodes.add(NodeDefinition.NULL);

        assertThat(runningNodes.size(), is(0));
    }

}
