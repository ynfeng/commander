package com.github.ynfeng.commander.server;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.fail;

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
        assertThat(getLogMessage(0), containsString("Bootstrap [1/1]: test started in"));
    }

    @Test
    public void should_log_infomation_when_occurs_exception_on_startup() throws Exception {
        Server server = Server.builder()
            .withName("local")
            .withAddress(Address.of("127.0.0.1", 1234))
            .withStartStep("test", () -> {
                throw new RuntimeException();
            })
            .build();

        try {
            server.startup();
            fail("Should throw exception.");
        } catch (Exception e) {
            assertThat(e, notNullValue());
        }

        assertThat(getLogMessage(0), is("Bootstrap test [1/1] failed with unexpected exception."));
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
        assertThat(getLogMessage(1), containsString("Shutdown [1/1]: test in"));
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

        assertThat(getLogMessage(1), is("Shutdown test [1/1] failed with unexpected exception."));
    }

    private static String getLogMessage(int index) {
        List<LoggingEvent> events = TestableLoggerAppender.getEvents();
        LoggingEvent loggingEvent = events.get(index);
        return loggingEvent.getFormattedMessage();
    }
}
