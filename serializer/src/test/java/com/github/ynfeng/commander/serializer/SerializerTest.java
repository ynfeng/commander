package com.github.ynfeng.commander.serializer;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SerializerTest {
    private Serializer serializer;

    @BeforeEach
    public void setup() {
        serializer = Serializer.create(SerializationTypes
            .builder()
            .startId(1)
            .add(String.class)
            .add(TestObject.class)
            .build());
    }

    @Test
    public void should_encode_and_decode_string() {
        byte[] bytes = serializer.encode("hello");
        String obj = serializer.decode(bytes);

        assertThat(obj, is("hello"));
    }

    @Test
    public void should_encode_and_decode_object() {
        TestObject obj = new TestObject(1);

        byte[] bytes = serializer.encode(obj);
        TestObject result = serializer.decode(bytes);

        assertThat(result.getId(), is(1));
    }

}
