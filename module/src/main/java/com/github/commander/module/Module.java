package com.github.commander.module;

import com.github.ynfeng.commander.support.Named;

public interface Module extends Named {
    void init();

    <T extends Component> Component getComponent(Class<T> componentType);
}
