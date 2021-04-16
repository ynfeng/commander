package com.github.ynfeng.commander.definition;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.Test;

class NodeDefinitionTest {

    @Test
    void ref_name_should_null_given_null_node() {
        assertThat(NodeDefinition.NULL.refName(), is("NULL_NODE"));
    }

    @Test
    void should_not_equals_with_different_class() {
        ForkDefinition fork = new ForkDefinition("aFork");
        assertThat(fork, not("fork"));
    }

    @Test
    void should_equals_when_same_ref_name() {
        ForkDefinition n1 = new ForkDefinition("aFork");
        ForkDefinition n2 = new ForkDefinition("aFork");
        assertThat(n1, is(n2));
    }

    @Test
    void should_same_hash_code_when_same_ref_name() {
        ForkDefinition n1 = new ForkDefinition("aFork");
        ForkDefinition n2 = new ForkDefinition("aFork");
        assertThat(n1.hashCode(), is(n2.hashCode()));
    }

    @Test
    void should_0_when_null_ref_name() {
        ForkDefinition n1 = new ForkDefinition(null);
        assertThat(n1.hashCode(), is(0));
    }

    @Test
    void should_not_equals_with_null() {
        ForkDefinition n1 = new ForkDefinition(null);
        assertThat(n1.equals(null), is(false));
    }

    @Test
    void should_equals_with_same_instance() {
        ForkDefinition n1 = new ForkDefinition(null);
        assertThat(n1, is(n1));
    }

}
