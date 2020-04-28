package com.github.ynfeng.commander.definition;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class NodeDefinitionTest {

    @Test
    public void should_throw_exception_when_set_next_on_null_node() {
        assertThrows(UnsupportedOperationException.class, () -> {
            NodeDefinition.NULL.next(NodeDefinition.NULL);
        });
    }

}
