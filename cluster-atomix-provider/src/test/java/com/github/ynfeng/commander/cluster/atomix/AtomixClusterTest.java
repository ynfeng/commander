package com.github.ynfeng.commander.cluster.atomix;

import static com.google.common.base.Preconditions.checkNotNull;

import com.github.ynfeng.commander.logger.CmderLogger;
import com.github.ynfeng.commander.logger.CmderLoggerFactory;
import com.google.common.io.Resources;
import io.atomix.core.Atomix;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;

class AtomixClusterTest {

    @Test
    public void should_get_atomix_version() throws IOException {
        String BUILD = Resources.toString(checkNotNull(Atomix.class.getClassLoader().getResource("VERSION"),
            "VERSION resource is null"), StandardCharsets.UTF_8);
        CmderLogger logger = CmderLoggerFactory.getServerLogger();
        logger.debug("============" + BUILD);
    }
}
