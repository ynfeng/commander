package com.github.commander.module;

import com.github.ynfeng.commander.support.Config;

public interface ModuleConfig extends Config {
    <C extends ComponentConfig> C getComponentConfig(String componentName);
}
