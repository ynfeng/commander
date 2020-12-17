package com.github.ynfeng.commander.cluster.communicate;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.Test;

class MessageTest {

    @Test
    public void should_create_message() {
        MessagingService.Message message = new MessagingService.Message("testType", new byte[] {0});

        assertThat(message.getType(), is("testType"));
        assertThat(message.getPayload(), is(new byte[] {0}));
    }

}
