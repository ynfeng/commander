package com.github.ynfeng.commander.core;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.is;

import com.google.common.collect.Maps;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.mvel2.MVEL;

public class MVELTest {

    @Test
    public void should_eval() {
        Map<String, Object> vars = Maps.newHashMap();
        vars.put("foo", 9);

        boolean greater = (boolean) MVEL.eval("foo > 3", vars);

        assertThat(greater, is(true));
    }
}
