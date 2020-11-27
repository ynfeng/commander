package com.github.ynfeng.commander.definition;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.Test;

class NodeDefinitionTest {

    @Test
    public void ref_name_should_null_given_null_node() {
        assertThat(NodeDefinition.NULL.refName(), is("NULL_NODE"));
    }

    @Test
    public void should_equals_when_same_ref_name() {
        ForkDefinition n1 = new ForkDefinition("aFork");
        ForkDefinition n2 = new ForkDefinition("aFork");
        assertThat(n1, is(n2));
    }

    @Test
    public void should_same_hash_code_when_same_ref_name(){
        ForkDefinition n1 = new ForkDefinition("aFork");
        ForkDefinition n2 = new ForkDefinition("aFork");
        assertThat(n1.hashCode(), is(n2.hashCode()));
    }

    @Test
    public void should_0_when_null_ref_name(){
        ForkDefinition n1 = new ForkDefinition(null);
        assertThat(n1.hashCode(), is(0));
    }

}
