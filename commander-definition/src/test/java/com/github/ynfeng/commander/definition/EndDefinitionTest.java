package com.github.ynfeng.commander.definition;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class EndDefinitionTest {

    @Test
    public void should_throw_exception_when_set_next() {
        assertThrows(UnsupportedOperationException.class, () -> {
            EndDefinition.create().next(NodeDefinition.NULL);
        });
    }

}
