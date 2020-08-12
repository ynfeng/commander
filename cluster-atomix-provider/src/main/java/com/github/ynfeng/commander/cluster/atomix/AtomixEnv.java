package com.github.ynfeng.commander.cluster.atomix;

import com.github.ynfeng.commander.cluster.Environment;
import java.io.InputStream;
import java.util.Map;
import org.mvel2.MVEL;
import org.yaml.snakeyaml.Yaml;

public class AtomixEnv implements Environment {
    private final Map data;

    public AtomixEnv() {
        data = loadYaml();
    }

    private Map loadYaml() {
        Yaml yaml = new Yaml();
        InputStream inputStream = getClass()
            .getClassLoader()
            .getResourceAsStream("commander-cluster.yaml");
        return yaml.load(inputStream);
    }

    @Override
    public <T> T getProperty(String name, T defaultValue) {
        try {
            T result = (T) MVEL.eval(name, data);
            return result == null ? defaultValue : result;
        } catch (Exception e) {
            return defaultValue;
        }
    }
}
