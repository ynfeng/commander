package com.github.ynfeng.commander.serializer;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import com.google.common.collect.Lists;
import org.junit.jupiter.api.Test;

class SerializationTypesTest {

    @Test
    public void should_create_serialization_type() {
        SerializationTypes types = SerializationTypes
            .builder()
            .startId(1)
            .add(String.class)
            .build();

        assertThat(types.startId(), is(1));
        assertThat(types.supportTypes(), is(Lists.newArrayList(String.class)));
        assertThat(types, notNullValue());
    }

    @Test
    public void should_create_serialization_with_basic_types() {
        SerializationTypes types = SerializationTypes
            .builder()
            .startId(1)
            .add(SerializationTypes.BASIC)
            .build();

        assertThat(types.startId(), is(1));
        assertThat(types.supportTypes(), is(Lists.newArrayList(byte[].class)));
        assertThat(types, notNullValue());
    }

}
