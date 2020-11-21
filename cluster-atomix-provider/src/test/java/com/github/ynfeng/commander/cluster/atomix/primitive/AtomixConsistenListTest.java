package com.github.ynfeng.commander.cluster.atomix.primitive;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.github.ynfeng.commander.cluster.Cluster;
import com.github.ynfeng.commander.cluster.atomix.AtomixClusterTestSupport;
import com.github.ynfeng.commander.cluster.primitive.DistributedList;
import com.github.ynfeng.commander.cluster.primitive.PrimitiveFactory;
import com.google.common.collect.Lists;
import java.util.function.Consumer;
import org.junit.jupiter.api.Test;

class AtomixConsistenListTest  extends AtomixClusterTestSupport {

    @Test
    public void should_add(){
        doWithList(list->{
            list.add("test");

            assertThat(list.size(), is(1));
        });
    }

    @Test
    public void should_to_list(){
        doWithList(list->{
            list.add("1");
            list.add("2");

            assertThat(list.toList(), is(Lists.newArrayList("1","2")));
        });
    }

    private <E> void doWithList(Consumer<DistributedList<E>> consumer) {
        Cluster cluster = getCluster();
        cluster.startup();
        PrimitiveFactory primitiveFactory = cluster.getGetPrimitiveFactory();
        DistributedList<E> list = primitiveFactory.createDistributedList("test_list");
        consumer.accept(list);
        list.destory();
        cluster.shutdown();
    }
}
