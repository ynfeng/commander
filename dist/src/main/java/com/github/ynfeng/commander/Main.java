package com.github.ynfeng.commander;

import com.github.ynfeng.commander.bootstrap.CommanderBootrstap;
import com.github.ynfeng.commander.cluster.SPIClusterProviderLoader;

public final class Main {
    private Main() {

    }

    public static void main(String[] args) throws Exception {
        CommanderBootrstap bootrstap = new CommanderBootrstap(new SPIClusterProviderLoader());
        bootrstap.bootstrap();
    }
}
