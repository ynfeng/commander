package com.github.ynfeng.commander.server;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;

import org.junit.jupiter.api.Test;

public class ServerTest {

    @Test
    public void should_create_server_from_builder() {
        Server server = Server.builder()
            .withName("local")
            .build();

        assertThat(server.name(), is("local"));
        assertThat(server, notNullValue());
    }
}
