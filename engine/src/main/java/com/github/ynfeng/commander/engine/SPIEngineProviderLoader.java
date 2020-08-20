package com.github.ynfeng.commander.engine;

import java.util.Iterator;
import java.util.Optional;
import java.util.ServiceLoader;

public class SPIEngineProviderLoader implements EngineProviderLoader {
    @Override
    public Optional<EngineProvider> load() {
        ServiceLoader<EngineProvider> engineProviders = ServiceLoader.load(EngineProvider.class);
        Iterator<EngineProvider> it = engineProviders.iterator();
        if (it.hasNext()) {
            return Optional.of(it.next());
        }
        return Optional.empty();
    }
}
