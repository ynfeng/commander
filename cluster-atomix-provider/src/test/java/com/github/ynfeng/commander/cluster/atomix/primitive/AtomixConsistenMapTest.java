package com.github.ynfeng.commander.cluster.atomix.primitive;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import com.github.ynfeng.commander.cluster.Cluster;
import com.github.ynfeng.commander.cluster.atomix.AtomixClusterTestSupport;
import com.github.ynfeng.commander.cluster.primitive.DistributedMap;
import com.github.ynfeng.commander.cluster.primitive.PrimitiveFactory;
import java.util.function.Consumer;
import org.junit.jupiter.api.Test;

class AtomixConsistenMapTest extends AtomixClusterTestSupport {

    @Test
    public void should_put_and_get() {
        doWithMap(map -> {
            map.put("test", "value");
            assertThat(map.get("test"), is("value"));
        });
    }

    @Test
    public void should_update() {
        doWithMap(map -> {
            map.put("test", "value");
            map.put("test1", "test1");
            map.update("test", "other");

            assertThat(map.get("test"), is("other"));
            assertThat(map.get("test1"), is("test1"));
        });
    }

    @Test
    public void should_remove() {
        doWithMap(map -> {
            map.put("test", "value");
            map.remove("test");

            assertThat(map.get("test"), nullValue());
        });
    }

    private <K, V> void doWithMap(Consumer<DistributedMap<K, V>> consumer) {
        Cluster cluster = getCluster();
        cluster.startup();
        PrimitiveFactory primitiveFactory = cluster.getGetPrimitiveFactory();
        DistributedMap<K, V> map = primitiveFactory.createDistributedMap("test");
        consumer.accept(map);
        map.destory();
        cluster.shutdown();
    }

}
