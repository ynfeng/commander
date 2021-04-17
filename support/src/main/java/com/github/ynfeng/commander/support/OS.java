package com.github.ynfeng.commander.support;

import java.util.Locale;

public class OS {

    private OS() {
    }

    public static boolean isLinux() {
        String os = System.getProperty("os.name", "generic").toLowerCase(Locale.ENGLISH);
        return os.contains("nux");
    }
}
