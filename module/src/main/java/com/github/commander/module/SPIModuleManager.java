package com.github.commander.module;

import com.google.common.collect.Maps;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.concurrent.atomic.AtomicBoolean;

public class SPIModuleManager implements ModuleManager {
    private final Map<Class<? extends Module>, Module> modulesRegistry = Maps.newHashMap();
    private AtomicBoolean isLoad = new AtomicBoolean();

    @Override
    public <T extends Module> T getModule(Class<T> moduleType) {
        tryLoadModules();
        Module module = modulesRegistry.get(moduleType);
        if (module == null) {
            throw new ModuleException(
                String.format("no such module '%s'", moduleType.getName()));
        }
        return (T) module;
    }

    private void tryLoadModules() {
        if (isLoad.compareAndSet(false, true)) {
            for (Module module : ServiceLoader.load(Module.class)) {
                modulesRegistry.put(module.getClass(), module);
            }
        }
    }
}
