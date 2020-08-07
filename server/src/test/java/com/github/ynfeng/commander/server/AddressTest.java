package com.github.ynfeng.commander.server;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.Test;

class AddressTest {

    @Test
    public void should_not_equals_other_instance(){
        Address addr = Address.of("127.0.0.1", 1234);
        assertThat(addr, not(new Object()));
    }

    @Test
    public void should_same_hash_code(){
        Address addr = Address.of("127.0.0.1", 1234);
        Address otherAddr = Address.of("127.0.0.1", 1234);

        assertThat(addr.hashCode(), is(otherAddr.hashCode()));
    }

}
