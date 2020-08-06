package com.github.ynfeng.commander.server;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.core.status.OnConsoleStatusListener;
import ch.qos.logback.core.status.StatusManager;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.slf4j.LoggerFactory;

public class ServerTest {

    @Test
    public void should_create_server_from_builder() {
        Server server = Server.builder()
            .withName("local")
            .withAddress(Address.of("127.0.0.1", 1234))
            .build();

        assertThat(server.name(), is("local"));
        assertThat(server.address(), is(Address.of("127.0.0.1", 1234)));
        assertThat(server, notNullValue());
    }

    @Test
    public void should_execute_startup_steps() {
        StartFunction startFunction = Mockito.mock(StartFunction.class);
        Server server = Server.builder()
            .withName("local")
            .withAddress(Address.of("127.0.0.1", 1234))
            .withStartStep("test", startFunction)
            .build();

        server.startup();

        Mockito.verify(startFunction).execute();
    }

    @Test
    public void should_shutdown() throws Exception {
        AutoCloseable closeable = Mockito.mock(AutoCloseable.class);
        StartFunction startFunction = () -> closeable;
        Server server = Server.builder()
            .withName("local")
            .withAddress(Address.of("127.0.0.1", 1234))
            .withStartStep("test", startFunction)
            .build();
        server.startup();

        server.shutdown();

        Mockito.verify(closeable).close();
    }

    @Test
    public void should_log_infomation_when_occurs_exception_on_shutdown() throws Exception {
        AutoCloseable closeable = () -> {
            throw new RuntimeException();
        };
        StartFunction startFunction = () -> closeable;
        Server server = Server.builder()
            .withName("local")
            .withAddress(Address.of("127.0.0.1", 1234))
            .withStartStep("test", startFunction)
            .build();
        LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
        StatusManager statusManager = lc.getStatusManager();
        OnConsoleStatusListener onConsoleListener = new OnConsoleStatusListener();
        statusManager.add(onConsoleListener);

        server.startup();

        server.shutdown();
    }
}
