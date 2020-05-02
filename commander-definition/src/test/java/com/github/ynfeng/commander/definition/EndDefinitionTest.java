package com.github.ynfeng.commander.definition;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class EndDefinitionTest {

    @Test
    public void should_throw_exception_when_set_next() {
        assertThrows(UnsupportedOperationException.class, () -> {
            new EndDefinition("end").next(NodeDefinition.NULL);
        });
    }

}
