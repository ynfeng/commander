package com.github.ynfeng.commander.engine;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.github.ynfeng.commander.definition.NodeDefinition;
import org.junit.jupiter.api.Test;

class ReadyNodesTest {

    @Test
    public void should_not_add_null_and_null_NULL_definition(){
        ReadyNodes readyNodes = new ReadyNodes();
        readyNodes.add(null);
        readyNodes.add(NodeDefinition.NULL);

        assertThat(readyNodes.size(), is(0));
    }

}
