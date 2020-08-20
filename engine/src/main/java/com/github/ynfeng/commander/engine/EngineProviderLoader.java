package com.github.ynfeng.commander.engine;

import java.util.Optional;

public interface EngineProviderLoader {
    Optional<EngineProvider> load();
}
