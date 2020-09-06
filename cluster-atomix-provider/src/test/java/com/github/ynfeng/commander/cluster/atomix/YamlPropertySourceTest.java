package com.github.ynfeng.commander.cluster.atomix;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.Test;

class YamlPropertySourceTest {

    @Test
    public void should_load_config_file_when_assign_profile() {
        System.setProperty("cmder.profile", "test");
        YamlPropertySource propertySource = new YamlPropertySource();

        String clusterId = propertySource.getProperty("cluster.id");

        assertThat(clusterId, is("test"));
    }

    @Test
    public void should_load_default_config_file_when_not_assign_profile() {
        System.setProperty("cmder.profile", "");
        YamlPropertySource propertySource = new YamlPropertySource();

        String clusterId = propertySource.getProperty("cluster.id");

        assertThat(clusterId, is("standalong"));
    }
}
