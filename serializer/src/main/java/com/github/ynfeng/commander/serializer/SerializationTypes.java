package com.github.ynfeng.commander.serializer;

import com.google.common.collect.Lists;
import java.util.Collections;
import java.util.List;

public class SerializationTypes {
    private final List<Class<?>> supportTypes = Lists.newArrayList();
    private int startId;

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
        private SerializationTypes types = new SerializationTypes();

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
