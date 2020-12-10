package com.github.ynfeng.commander.serializer;

import com.google.common.collect.Lists;
import java.util.Collections;
import java.util.List;

public class SerializationTypes {
    public static final int CUSTOM_ID = 200;
    private final List<Class<?>> supportTypes = Lists.newArrayList();
    private int startId = CUSTOM_ID;

    public static SerializationTypesBuilder builder() {
        return new SerializationTypesBuilder();
    }

    public int startId() {
        return startId;
    }

    public List<Class<?>> supportTypes() {
        return Collections.unmodifiableList(supportTypes);
    }

    public static class SerializationTypesBuilder {
        private final SerializationTypes types = new SerializationTypes();

        public SerializationTypesBuilder startId(int id) {
            types.startId = id;
            return this;
        }

        public SerializationTypesBuilder add(Class<?> type) {
            types.supportTypes.add(type);
            return this;
        }

        public SerializationTypes build() {
            return types;
        }
    }
}
