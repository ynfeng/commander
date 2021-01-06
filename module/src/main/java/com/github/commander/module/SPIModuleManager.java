package com.github.commander.module;

import com.google.common.collect.Maps;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.LockSupport;
import org.jetbrains.annotations.NotNull;

public class SPIModuleManager implements ModuleManager {
    private final Map<Class<? extends Module>, Module> modulesRegistry = Maps.newConcurrentMap();
    private final AtomicBoolean isLoad = new AtomicBoolean();
    private final AtomicBoolean isLoading = new AtomicBoolean();

    @Override
    @SuppressWarnings("unchecked")
    public <T extends Module> T getModule(Class<T> moduleType) {
        loadModules();
        busyWait();
        Module module = getModuleFromRegistryOrThrowException(moduleType);
        return (T) module;
    }

    @NotNull
    private <T extends Module> Module getModuleFromRegistryOrThrowException(Class<T> moduleType) {
        Module module = modulesRegistry.get(moduleType);
        if (module == null) {
            throw new ModuleException(
                String.format("no such module '%s'", moduleType.getName()));
        }
        return module;
    }

    private void busyWait() {
        while (!isLoad.get()) {
            Thread.yield();
            LockSupport.parkNanos(1000);
        }
    }

    private void loadModules() {
        if (!isLoad.get() && isLoading.compareAndSet(false, true)) {
            for (Module module : ServiceLoader.load(Module.class)) {
                modulesRegistry.put(module.getClass(), module);
            }
            isLoad.set(true);
            isLoading.set(false);
        }
    }
}
