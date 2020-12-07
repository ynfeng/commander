package com.github.commander.module;

import com.github.ynfeng.commander.support.Named;

public interface Module<C extends ModuleConfig> extends Named {
    void init(C config);

    <T extends Component> Component getComponent(Class<T> componentType);
}
