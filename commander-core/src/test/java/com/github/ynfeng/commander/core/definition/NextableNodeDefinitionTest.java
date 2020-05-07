package com.github.ynfeng.commander.core.definition;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.is;

import org.junit.jupiter.api.Test;

class NextableNodeDefinitionTest {

    @Test
    public void should_next() {
        NextableNodeDefinition nextable = new NextableNodeDefinition("nextable") {
        };
        nextable.next(new AbstractNodeDefinition("aNode") {
        });

        assertThat(nextable.next().refName(), is("aNode"));
    }

}
