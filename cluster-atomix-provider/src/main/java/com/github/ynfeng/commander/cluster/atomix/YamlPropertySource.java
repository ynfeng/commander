package com.github.ynfeng.commander.cluster.atomix;

import com.github.ynfeng.commander.cluster.PropertySource;
import com.google.common.base.Strings;
import java.io.InputStream;
import java.util.Map;
import org.mvel2.MVEL;
import org.yaml.snakeyaml.Yaml;

public class YamlPropertySource implements PropertySource {
    @SuppressWarnings("rawtypes")
    private final Map data;

    protected YamlPropertySource() {
        data = loadYaml();
    }

    @SuppressWarnings("rawtypes")
    private Map loadYaml() {
        String configFile = getConfigFile();
        Yaml yaml = new Yaml();
        InputStream inputStream = getClass()
            .getClassLoader()
            .getResourceAsStream(configFile);
        return yaml.load(inputStream);
    }

    private String getConfigFile() {
        return String.format("commander-%s.yaml", getProfile());
    }

    private String getProfile() {
        String profile = System.getProperty("cmder.profile");
        if (Strings.isNullOrEmpty(profile)) {
            return "cluster";
        }
        return profile;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getProperty(String name) {
        return (T) MVEL.eval(name, data);
    }
}
