package com.github.ynfeng.commander.server;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;

import ch.qos.logback.classic.spi.LoggingEvent;
import com.github.ynfeng.commander.server.testutil.TestableLoggerAppender;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class ServerTest {

    @BeforeEach
    public void setup() {
        TestableLoggerAppender.reset();
    }

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
        server.startup();

        server.shutdown();

        List<LoggingEvent> events = TestableLoggerAppender.getEvents();

        LoggingEvent loggingEvent = events.get(0);

        assertThat(loggingEvent.getFormattedMessage(), is("Shutdown test [1/1] failed with unexpected exception."));
    }
}
