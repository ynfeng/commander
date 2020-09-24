package com.github.ynfeng.commander;

import com.github.ynfeng.commander.bootstrap.CommanderServer;
import com.github.ynfeng.commander.cluster.SPIClusterProviderLoader;

public final class Main {
    private Main() {

    }

    public static void main(String[] args) throws Exception {
        CommanderServer bootrstap = new CommanderServer(new SPIClusterProviderLoader());
        bootrstap.bootstrap();
    }
}
