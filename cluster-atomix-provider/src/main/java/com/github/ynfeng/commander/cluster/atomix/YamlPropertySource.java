package com.github.ynfeng.commander.cluster.atomix;

import com.github.ynfeng.commander.cluster.PropertySource;
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
        Yaml yaml = new Yaml();
        InputStream inputStream = getClass()
            .getClassLoader()
            .getResourceAsStream("commander-cluster.yaml");
        return yaml.load(inputStream);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getProperty(String name) {
        return (T) MVEL.eval(name, data);
    }
}
